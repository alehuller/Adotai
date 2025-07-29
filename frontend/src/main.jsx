// Conecta o React ao DOM

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

const rootElement = document.getElementById('root');
if (!rootElement) {
  console.error('ERRO FATAL: Elemento #root não encontrado no index.html');
  throw new Error('Elemento #root não encontrado');
}

console.log("1. main.jsx carregado com sucesso");
ReactDOM.createRoot(rootElement).render(<App />);