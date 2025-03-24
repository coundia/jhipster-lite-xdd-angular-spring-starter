import { environment } from './environment';

describe('Environment Configuration', () => {
  it('should have production mode disabled', () => {
    expect(environment.production).toBeFalsy();
  });
});
