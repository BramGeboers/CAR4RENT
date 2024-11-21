const { i18n } = require("./next-i18next.config");

module.exports = {
  reactStrictMode: true,
  output: 'standalone',
  i18n,
  env: {
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL,
    NEXT_PUBLIC_SOCKETIO_URL: process.env.NEXT_PUBLIC_SOCKETIO_URL,
  },
};

