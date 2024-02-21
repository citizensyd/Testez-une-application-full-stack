describe('Redirection to not found', () => {
  it('should redirect to not found page', () => {
    cy.visit('/test-not-found');
    cy.url().should('include', '/404');
    cy.contains('h1', 'Page not found !').should('exist');
  })
})
