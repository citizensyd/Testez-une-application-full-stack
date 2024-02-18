describe('Create Session Spec', () => {
  it('should create session after admin authentication', () => {
    // Visitez la page de connexion
    cy.visit('/login');

    // Interception de la requête de connexion et simulation d'une réponse réussie avec un utilisateur administrateur
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'adminUser',
        firstName: 'Admin',
        lastName: 'User',
        admin: true
      }
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    // Connectez-vous avec les identifiants administrateurs
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234"}{enter}{enter}');

    // Vérifiez que vous êtes redirigé vers la page de gestion des sessions après l'authentification
    cy.url().should('include', '/sessions');

    // Ajoutez l'interception ici, avant d'interagir avec le `mat-select`
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' },
        // ... autres données simulées de professeurs si nécessaire
      ],
    }).as('getTeachers');

    // Cliquez sur le bouton "Create" pour accéder à la page de création de session
    cy.get('button[routerlink="create"]').click();

    // Interception de la requête de création de session et simulation d'une réponse réussie
    cy.intercept('POST', '/api/session', {
      statusCode: 200,
      body: {
        "id": 3,
        "name": "session 1",
        "date": "2012-01-01T00:00:00.000+00:00",
        "teacher_id": null,
        "description": "my description",
        "users": [],
        "createdAt": "2024-02-17T16:56:56.6730881",
        "updatedAt": "2024-02-17T16:56:56.7070921"
      }
    }).as('createSession');

    // Remplissez le formulaire avec les données de session
    cy.get('input[formControlName=name]').type('Session Test');
    cy.get('input[formControlName=date]').invoke('val', '2024-02-16').trigger('change');
    cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains(' Margot DELAHAYE ').click();
    cy.get('textarea[formControlName=description]').type('Test session description');

    // Soumettez le formulaire
    cy.get('form').submit()

    // Vérifiez que vous êtes redirigé vers la page des sessions après la création de session
    cy.url().should('include', '/sessions');
  });
});
