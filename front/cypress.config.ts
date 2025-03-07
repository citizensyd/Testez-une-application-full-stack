import { defineConfig } from "cypress";

export default defineConfig({
  projectId: 'ov6n9p',
  videosFolder: "cypress/videos",
  screenshotsFolder: "cypress/screenshots",
  fixturesFolder: "cypress/fixtures",
  video: false,

  e2e: {
    // We've imported your old cypress plugins here.
    // You may want to clean this up later by importing these.
    setupNodeEvents(on, config) {
      return require("./cypress/plugins/index.ts").default(on, config);
    },
    baseUrl: "http://localhost:4200",
  },

  component: {
    devServer: {
      framework: "angular",
      bundler: "webpack",
    },
    specPattern: ["**/*.cy.ts", "**/*.spec.ts"],
  },
});
