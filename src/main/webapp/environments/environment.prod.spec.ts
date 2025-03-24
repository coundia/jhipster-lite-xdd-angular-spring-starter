import { environment } from './environment.prod';

describe('Environment Configuration (prod)', () => {
  it('should have production mode enabled', () => {
    expect(environment.production).toBeTruthy();
  });
});
