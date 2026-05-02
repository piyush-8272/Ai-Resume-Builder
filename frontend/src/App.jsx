import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import Navbar from './components/Navbar'
import UploadPage from './pages/UploadPage'
import ResultPage from './pages/ResultPage'
import HistoryPage from './pages/HistoryPage'

export default function App() {
  return (
    <Router>
      <Toaster
        position="top-right"
        toastOptions={{
          style: { fontFamily: 'Inter, sans-serif', fontSize: 14 },
          success: { iconTheme: { primary: '#10b981', secondary: '#fff' } },
        }}
      />
      <div className="app">
        <Navbar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<UploadPage />} />
            <Route path="/result/:id" element={<ResultPage />} />
            <Route path="/history" element={<HistoryPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}
