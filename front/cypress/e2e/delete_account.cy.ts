describe('Register Spec', () => {
  const user = {
    id: 1,
    email: 'newuser@example.com',
    firstName: 'New',
    lastName: 'User'
  }

  it('Login user successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("newuser@example.com")
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)

    cy.url().should('include', '/sessions')

    cy.intercept('DELETE', `/api/user/${user.id}`, {
      statusCode: 200,
    }).as('deleteAccount');

    cy.intercept('GET', `/api/user/${user.id}`, {
      statusCode: 200,
      body: user
    }).as('userInfo');

    cy.contains('span', 'Account').click();
    cy.contains('button', 'Detail').click();

    cy.wait('@deleteAccount').its('response.statusCode').should('eq', 200);
  })
});
