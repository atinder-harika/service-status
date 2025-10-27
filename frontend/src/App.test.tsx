import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import App from './App';

describe('App', () => {
  it('renders Service Status heading', () => {
    render(<App />);
    const heading = screen.getByRole('heading', { name: /service status/i });
    expect(heading).toBeInTheDocument();
  });
});
