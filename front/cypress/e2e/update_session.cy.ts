describe('Create and Update Session Spec', () => {
  beforeEach(() => {
    // Interceptions communes pour les tests
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 1, username: 'adminUser', firstName: 'Admin', lastName: 'User', admin: true }
    }).as('login');

    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [{ id: 1, firstName: 'Margot', lastName: 'DELAHAYE' }]
    }).as('getTeachers');

    // Connexion initiale et préparation de l'environnement de test
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234"}{enter}{enter}');
    cy.url().should('include', '/sessions');
  });

  it('should create and update a session', () => {
    // Données de la session à créer
    const newSessionData = {
      "id": 3,
      "name": "Session Test",
      "date": "2024-02-16",
      "teacher_id": 1,
      "description": "Test session description",
      "users": [],
      "createdAt": "2024-02-17T16:56:56.6730881",
      "updatedAt": "2024-02-17T16:56:56.7070921"
    };

    const updateSessionData = {
      "name": "Update Name"
    }

    // Interception pour la création de la session
    cy.intercept('POST', '/api/session', {
      statusCode: 200,
      body: newSessionData
    }).as('createSession');

    // Interception GET pour récupérer le tableau de sessions avec la session nouvellement créée
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [newSessionData]
    }).as('getSessions');

    cy.intercept('GET', `/api/session/${newSessionData.id}`, {
      statusCode: 200,
      body: newSessionData
    }).as('getSession');

    // Création de la session
    cy.get('button[routerlink="create"]').click();
    cy.get('input[formControlName=name]').type(newSessionData.name);
    cy.get('input[formControlName=date]').invoke('val', newSessionData.date).trigger('change');
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type(newSessionData.description);
    cy.get('form').submit();
    cy.wait('@createSession');

    // Interception pour la suppression de la session
    cy.intercept('PUT', `/api/session/${newSessionData.id}`, {
      statusCode: 200
    }).as('updateSession');

    cy.contains('button', 'Close').click();

    // Suppression de la session
    cy.contains('button', 'Edit').click();

    // Changer le nom de la session
    cy.get('input[formControlName=name]').type(updateSessionData.name);

    // Suppression de la session
    cy.contains('button', 'Save').click();

    cy.wait('@updateSession').its('response.statusCode').should('eq', 200);

    // Vérification du retour à la page des sessions
    cy.url().should('include', '/sessions');
  });
});
