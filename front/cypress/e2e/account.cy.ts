describe('Create Session Spec', () => {
  // Constantes pour les URLs et les sélecteurs
  const loginUrl = '/login';
  const sessionsUrl = '/sessions';
  const emailSelector = 'input[formControlName=email]';
  const passwordSelector = 'input[formControlName=password]';

  const userAdmin = {
    "id": 1,
    "email": "yoga@studio.com",
    "lastName": "Admin",
    "firstName": "Admin",
    "admin": true,
    "createdAt": "2024-02-16T08:44:48",
    "updatedAt": "2024-02-16T08:44:48"
  }

  beforeEach(() => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    // Interception des requêtes
    cy.intercept('GET', '/api/user/1', {
      body: userAdmin
    });
  });

  it('should present user information', () => {
    // Visite de la page de connexion
    cy.visit(loginUrl);

    // Connexion avec les identifiants administrateurs
    cy.get(emailSelector).type('yoga@studio.com');
    cy.get(passwordSelector).type('test!1234"}{enter}{enter}');

    // Vérification de la redirection vers la page de gestion des sessions
    cy.url().should('include', sessionsUrl);

    // Accès à la page de création de session
    cy.contains('span', 'Account').click();

    cy.url().should('include', 'me');

    cy.contains('p', userAdmin.email).should('exist');
    cy.contains('p', userAdmin.lastName).should('exist');
    cy.contains('p', userAdmin.firstName).should('exist');
    cy.contains('p', 'You are admin').should('exist');
    cy.contains('p', 'Create at: February 16, 2024').should('exist');
    cy.contains('p', 'Last update: February 16, 2024').should('exist');


  });
});
