# OnlineStore
___
**OnlineStore** - Module for interacting with the database of products.

### Patterns and APIs implemented in this module:
- Maven ```4.0.0```
- Spring Boot ```2.7.8```
  - Spring Security5
  - Spring Data JPA
  - Spring AOP
- JUnit & Mockito
- Lombok
- PostgreSQL
- H2 DataBase(for tests)
- Thymeleaf
- DTO pattern

## Description

The module is made for a visual display of the functionality of connected API, business logic is not implemented as in real conditions

From the user's side, this module is two pages (the main page and the variable product page). 
The main page contains:
- List of products with pagination implemented. Users can apply filters to clarify the request. 
- List of most viewed products.
- Form to add new product
- Login form.

The user who is authorized as an admin can go to the product page. This page contains:
- Product info
- Form to update this product
- Button to delete this product

On the main page, a section is available only for the admin user. 
This section contains a button to fix products added to the database, not through the UI form(details of these products are not initialized). 
I've decided to keep this issue to try to fix it through HTML-request from the UI(and I got it).
