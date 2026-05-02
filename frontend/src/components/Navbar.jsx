import { Link, useLocation } from 'react-router-dom'
import { FileText, History, Zap } from 'lucide-react'

export default function Navbar() {
  const { pathname } = useLocation()

  return (
    <nav className="navbar">
      <div className="navbar-inner">
        <Link to="/" className="navbar-brand">
          <div className="brand-icon">
            <Zap size={18} />
          </div>
          <span className="brand-name">
            Smart <span>Resume</span> Analyzer
          </span>
        </Link>

        <div className="navbar-links">
          <Link to="/" className={`nav-link ${pathname === '/' ? 'active' : ''}`}>
            <FileText size={15} />
            Analyze
          </Link>
          <Link to="/history" className={`nav-link ${pathname === '/history' ? 'active' : ''}`}>
            <History size={15} />
            History
          </Link>
        </div>
      </div>
    </nav>
  )
}
