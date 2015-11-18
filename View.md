# Introduction #

The view module provides a simple way to activate different parts of the application's user
interface. To help us explain the problem domain, let's start with defining what a view is. In
our case, a view is defined as an UI entity consisting of a set components.

Let us consider an example application which requires the user to login. Once the user is logged
in, the login screen is changed to the main layout of the application that consists of a header,
a navigation menu and the content. If we describe the same structure in terms of views, we would
most likely have a login view, a main view, a navigation view and an arbitrary amount of content
views. Each of the views are a UI entity, the login view contains the login form, the main view
contains the overall layout for the application, the navigation view contains a possibly a tree
or maybe buttons depending on the application's state and the content views contain the different
"pages" of the application. We won't define the header in our example as a view, because it is
static and is not dependent on the application's state. Hence, it is a static part of the main
view.

Views can exist in different levels, as in the example above, the main view actually consisted of
a navigation view and the content view. Often the component hierarchy can be deep in a Vaadin
application and accessing a specific component instance in the hierarchy tree can be hard.
Sometimes you need to access components which are not even in the same component tree path (in
other words, the component you want to access is not a parent nor a child to the accessing
component). The ViewHandler will help you in this task.

# Architecture #

The core of the view module is the ViewHandler, ViewContainers and the actual Views. The
ViewHandler is a class containing static methods for registering and activating views in an
application. You can call anywhere in the application `ViewHandler.activateView(yourViewId);`
and the ViewHandler will tell your view's parent to activate the view. The ViewHandler uses the
ThreadLocal pattern to keep track on application instance independent views. It implements the
TransactionListener interface and for it to be enable, the developer will need to register the
ViewHandler to his application instance. This is described in more detail later on in this
documentation.

Every view made by the developer should implement the View interface. The interface provides
the ViewHandler with methods for activating and deactivating views. A default implementation
(called AbstractView) of the View interface exists. The AbstractView itself extends
CustomComponent and this way we want to encourage the developers to keep their APIs clean and
simple. You can use the AbstractView as a basis for your views or then you can directly
implement the View interface in any way you like it.

The ViewContainer is an interface all "parents" to the views should implement. The interface
contains only one method, activate, which takes as input the view instance. The implementing
class is the responsible of adding the view to the proper layout or panel.

# Importing the view module to your project #
The view module does not have any dependencies, so all you need to do is to drop the jar package
in your application's WEB-INF/lib -folder.

# Configuring the view module #

All you need to do, is to register the ViewHandler to your application as a transaction listener.
This is usually done in the application's init-method.
```
public class YourApplication extends Application {
        @Override
        public void init() {
                ViewHandler.initialize(this);
            
                ...
        }
}
```

# View IDs and ViewItems #

Just like with data containers in Vaadin, the ViewHandler handles view ids and ViewItems. The view
id is a unique identifier for the view instance. If you only need one instance of a view, then it
is a good practice to use the view's class object as the view id. The benefit you get by using the
view class object as the view id, is that the ViewHandler will automatically use that class object
for instantiating view instances. If you decide to use some other object as the view id, then make
sure you specify the view class for the ViewItem.

Sometimes there is need to pass parameters to the constructor of the view or maybe there is a need
to do something else upon the instantiation of the view. For this purpose, there is the
`ViewFactory` interface. ViewFactory is a class which is responsible for instantiating the
view object in the ViewItem. By default, the ViewItem gets an implementation of the
`DefaultViewFactory`, which calls on the view class's default constructor to instantiate the
view object. For example, if you need to pass a parameter in the constructor, you can create your
own implementation of the `ViewFactory` interface and set the resulting factory as the
ViewItem's factory. This way, when the view is being instantiated, your view factory will be called
instead of the default one. A view item's factory is set by calling the `setFactory()` method.

Each view item has its own view factory. This means that one view item can have one implementation
of the `ViewFactory` interface and another view item can have another implementation. If
you want to use your own view factory for all the created view items, you can specify a default
view factory in the `ViewHandler`. Just call the `ViewHandler.setDefaultViewFactory()`
method with the desired implementation of the `ViewFactory` interface and any view item added
_after_ this will get your factory as it's default factory. Note that setting the default view
factory will _not_ affect existing view items.

A ViewItem is a simple class containing information about the view which was registered to the
ViewHandler. The ViewItem contains information about the view's class type, the view id and keeps a
record of the view instance.

# The activation process #

The activation process begins when the ViewHandler's `activateView()` is called with the view
id of the view we want to activate. The method also accepts optional parameters which are used as
the parameters for the view's `activated()`-method. Before starting the activation process, the
ViewHandler dispatches a preDispatch event notifying all listeners, that given view is being
activated. If one or more of the dispatch listeners throws a `DispatchException`, then the
dispatch process is canceled. If an exception is not thrown, the ViewHandler continues with
fetching an instance of the view.

If the view id is the class object of the view, then the class object is used for instantiating
the view. Otherwise the developer must call the `setViewClass` method to define the class
object which is to be used for instantiating the view object. Once the view instance has been
instantiated, the same instance will be used the next time and a new one will not be created.

Once the view instance is fetched, the view container is told to activate the view. After
activation, the view instance is told that is has been activated, this is done by calling
`activated(params)` on the view instance. The parameter `params` are the same parameters
as which were provided to the ViewHandler when the `activateView`-method was called.

Finally, the ViewHandler will dispatch an event telling the listeners that the view was activated.

Views support activation by URI fragments. You can specify a URI fragment for any view by calling
the `ViewHandler`'s `addUri()` method. Once a URI fragment has been defined for the view,
the view can be activated by adding the URI fragment to the URL.

Example
```
ViewHandler.addView(SomeView.class, parent);
ViewHandler.addUri("some", SomeView.class);
```

To trigger the activation of your view, enter the URL http://somedomain/yourapp#some to your
browser. Your application will behave just if `ViewHandler.activate(SomeView.class)` would
have been called. Views can also be activated programmatically so that also the URI changes. This
is done by providing a boolean parameter to the `activate()` method.

Note that you can use activation parameters in your URIs. For example, when entering the URL
http://somedomain/yourapp#some/foo/bar , the SomeView would get activated just like with the previous
URL, but this time the `activate()` method will get two string parameters, "foo" and "bar".

```
// This will activate SomeView AND change the URI fragment in the application
ViewHandler.activate(SomeView.class, true);
```

Note that for the URI fragments to be triggered, you need to add the UriFragmentUtility component
to the application. You can get the the component by calling `ViewHandler.getUriFragmentUtil()`.

# Code examples #
Creating your own view
```
public class CommentView extends AbstractView<VerticalLayout> implements ClickListener {
  private static final long serialVersionUID = 8401760557369059696L;

  private TextField comments;
  private Button sendCommentBtn;

  public CommentView() {
    // Give an instance of a VerticalLayout as the parameter. The
    // VerticalLayout instance will be stored in the AbstractView's
    // "content" parameter. The "content" paremeter is protected, 
    // so it can be accessed directly in subclasses, just as we've 
    // done below when adding the TextField and the Button.
    super(new VerticalLayout());

    comments = new TextField();
    comments.setCaption("Your comments");
    // content is the layout you provided in the super's constructor
    getContent().addComponent(comments);

    sendCommentBtn = new Button("Send", this);
    getContent().addComponent(sendCommentBtn);
  }

  ...

}
```

Creating a view container
```
public class MainLayout extends CustomComponent implements ViewContainer {

  private VerticalLayout layout = new VerticalLayout();
  private View currentView;

  public MainLayout() {
    setCompositionRoot(layout);
    ...
    // Register child views
    ViewHandler.addView(CommentView.class, this);
    ViewHandler.addView(EditingView.class, this);
    
    // Set the default view
    currentView = ViewHandler.getViewItem(CommentView.class).getView();
    layout.addComponent((Component) currentView);
    ...
  }

  ...

  public void activate(View view) {
    if(!(view instanceof Component)) {
       throw new IllegalArgumentException("View must be a component");
    }
    // Activate the view by replacing the current view with the given view in 
    // the main layout
    layout.replaceComponent((Component) currentView, (Component) view);
    currentView = view;
  }

  ...
}
```

Example of activating a view
```
public Foobar extends CustomComponent implements ClickListener {
  ...
  public void buttonClick(ClickEvent event) {
    ViewHandler.activateView(EditingView.class);
  }
  ...
}
```