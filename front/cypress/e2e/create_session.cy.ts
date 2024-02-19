describe('Create Session Spec', () => {
  // Constantes pour les URLs et les sélecteurs
  const loginUrl = '/login';
  const sessionsUrl = '/sessions';
  const emailSelector = 'input[formControlName=email]';
  const passwordSelector = 'input[formControlName=password]';
  const createButtonSelector = 'button[routerlink="create"]';

  beforeEach(() => {
    // Interception des requêtes
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 1, username: 'adminUser', firstName: 'Admin', lastName: 'User', admin: true }
    });

    cy.intercept('GET', '/api/session', []).as('session');
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [{ id: 1, firstName: 'Margot', lastName: 'DELAHAYE' }]
    }).as('getTeachers');

    cy.intercept('POST', '/api/session', {
      statusCode: 200,
      body: {
        "id": 3, "name": "session 1", "date": "2012-01-01T00:00:00.000+00:00",
        "teacher_id": null, "description": "my description", "users": [],
        "createdAt": "2024-02-17T16:56:56.6730881", "updatedAt": "2024-02-17T16:56:56.7070921"
      }
    }).as('createSession');
  });

  it('should create session after admin authentication', () => {
    // Visite de la page de connexion
    cy.visit(loginUrl);

    // Connexion avec les identifiants administrateurs
    cy.get(emailSelector).type('yoga@studio.com');
    cy.get(passwordSelector).type('test!1234"}{enter}{enter}');

    // Vérification de la redirection vers la page de gestion des sessions
    cy.url().should('include', sessionsUrl);

    // Accès à la page de création de session
    cy.get(createButtonSelector).click();

    // Remplissage du formulaire de session
    cy.get('input[formControlName=name]').type('Session Test');
    cy.get('input[formControlName=date]').invoke('val', '2024-02-16').trigger('change');
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains(' Margot DELAHAYE ').click();
    cy.get('textarea[formControlName=description]').type('Test session description');

    // Soumission du formulaire
    cy.get('form').submit();

    // Vérification de la redirection après création de la session
    cy.url().should('include', sessionsUrl);
  });
});
