module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    'plugin:vue/recommended',
    '@vue/typescript/recommended',
    'plugin:prettier/recommended',
    'prettier',
  ],
  plugins: ['prettier'],
  rules: {
    'prettier/prettier': 'error',
    'vue/multi-word-component-names': 'off',
  },
}