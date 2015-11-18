# Frequently Asked Questions #
**Q: When will the peristence module support Hibernate?**<br>
A: Quick answer would be that it won't. It is a deliberate decision not to use Hibernate and instead rely on EclipseLink. However, it shouldn't require too much work to convert the JPAFacade to use Hibernate.<br>
<br>
<b>Q: Why EclipseLink and not Hibernate?</b><br>
A: I've found EclipseLink's session management to be much more suitable for applications built on Vaadin. Secondly, EclipseLink is Sun Microsystem's choice for reference implementation of the JPA specification.