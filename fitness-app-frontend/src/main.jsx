import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import { Provider } from 'react-redux'
import {store} from './store/store'
import { AuthProvider } from 'react-oauth2-code-pkce'
import { authConfig } from './authConfig.js'

createRoot(document.getElementById('root')).render(
  <>
  <AuthProvider authConfig={authConfig}
    loadingComponent={<div>Loading....</div>}
  >
  <Provider store={store}>
    <StrictMode>
      <App />
    </StrictMode>
  </Provider>
  </AuthProvider>
  </>
)
