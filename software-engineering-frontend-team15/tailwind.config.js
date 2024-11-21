/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
  ],  theme: {
    extend: {
      backgroundImage: {
        'star': "url('/star.jpg')",
      },
      colors: {
        'primary': '#1976d2',
      },
      animation: {
        'ease-in-custom': 'ease-in 1s infinite',
        'ease-out-custom': 'ease-out 1s infinite'
      }
    },

      
  },
  plugins: [],
}

