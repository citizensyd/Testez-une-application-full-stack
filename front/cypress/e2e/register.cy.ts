describe('Register Spec', () => {
  it('Registration successful', () => {
    cy.visit('/register')

    // Interceptez la requête POST de l'inscription et simulez une réponse réussie
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        id: 1,
        email: 'newuser@example.com',
        firstName: 'New',
        lastName: 'User'
      },
    }).as('registerRequest')

    // Remplissez le formulaire d'inscription
    cy.get('input[formControlName=email]').type("newuser@example.com")
    cy.get('input[formControlName=firstName]').type("New")
    cy.get('input[formControlName=lastName]').type("User")
    cy.get('input[formControlName=password]').type("password")

    // Soumettez le formulaire
    cy.get('form').submit()

    // Vérifiez que la requête d'inscription a été faite
    cy.wait('@registerRequest')

    // Après une inscription réussie, vérifiez que l'utilisateur est redirigé vers la page de connexion
    cy.url().should('include', '/login')
  })
});
