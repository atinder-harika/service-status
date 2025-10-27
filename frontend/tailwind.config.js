/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'status-operational': '#16a34a',
        'status-degraded': '#fbbf24',
        'status-maintenance': '#3b82f6',
        'status-down': '#e74c3c',
      },
    },
  },
  plugins: [],
}
