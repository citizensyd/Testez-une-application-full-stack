describe('Login spec', () => {
  it('Must set empty input to red after first focus', () => {
    cy.visit('/login');

    cy.get('[formcontrolname="email"]').focus();

    cy.get('[formcontrolname="email"]').blur();

    cy.get('[formcontrolname="email"]').should('have.css', 'caret-color', 'rgb(244, 67, 54)'); // Correspond à la valeur RGB #f44336

    cy.get('[formcontrolname="password"]').focus();

    cy.get('[formcontrolname="password"]').blur();

    cy.get('[formcontrolname="password"]').should('have.css', 'caret-color', 'rgb(244, 67, 54)'); // Assurez-vous d'adapter ce sélecteur et cette propriété CSS à votre implémentation spécifique
  });
  it('Button should be disabled if either of the two inputs is empty', () => {

    cy.visit('/login');

    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('[formcontrolname="email"]').type('example@email.com');

    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('[formcontrolname="email"]').clear();

    cy.get('[formcontrolname="password"]').type('password123');

    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('[formcontrolname="email"]').type('example@email.com');

    cy.get('button[type="submit"]').should('not.be.disabled');
  });
  it('Should keep the caret red if email format is invalid and verify that the password contains at least three characters', () => {
    cy.visit('/login');

    cy.get('[formcontrolname="email"]').focus().type('invalid_email');

    cy.get('[formcontrolname="email"]').blur();

    cy.get('[formcontrolname="email"]').should('have.css', 'caret-color', 'rgb(244, 67, 54)');

    cy.get('[formcontrolname="email"]').clear().type('example@email.com');

    cy.get('[formcontrolname="email"]').blur();

    cy.get('[formcontrolname="email"]').should('have.css', 'caret-color', 'rgb(63, 81, 181)');

    cy.get('[formcontrolname="password"]').focus();

    cy.get('[formcontrolname="password"]').blur();

    cy.get('[formcontrolname="password"]').should('have.css', 'caret-color', 'rgb(244, 67, 54)');

    cy.get('[formcontrolname="password"]').type('password123');

    cy.get('[formcontrolname="password"]').blur();

    cy.get('[formcontrolname="password"]').should('have.css', 'caret-color', 'rgb(63, 81, 181)');

  });
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});
