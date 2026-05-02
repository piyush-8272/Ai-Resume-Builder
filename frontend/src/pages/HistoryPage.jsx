import { useState, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { Eye, History, Plus } from 'lucide-react'
import toast from 'react-hot-toast'
import { getHistory } from '../services/api'

function scoreClass(score) {
  if (score >= 80) return 'excellent'
  if (score >= 60) return 'good'
  if (score >= 40) return 'average'
  return 'poor'
}

const scoreColor = {
  excellent: 'var(--success-500)',
  good:      '#3b82f6',
  average:   'var(--warning-500)',
  poor:      'var(--danger-500)',
}

export default function HistoryPage() {
  const navigate = useNavigate()
  const [history, setHistory]   = useState([])
  const [loading, setLoading]   = useState(true)

  useEffect(() => {
    getHistory()
      .then(setHistory)
      .catch(() => toast.error('Failed to load history.'))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="animate-in">
      <div className="history-header">
        <div>
          <h1 style={{ fontSize: 26, fontWeight: 800, color: 'var(--gray-900)', letterSpacing: '-.01em' }}>
            Analysis History
          </h1>
          <p style={{ color: 'var(--text-muted)', marginTop: 4, fontSize: 14 }}>
            {history.length} total {history.length === 1 ? 'analysis' : 'analyses'}
          </p>
        </div>
        <Link to="/" className="btn btn-primary">
          <Plus size={15} /> New Analysis
        </Link>
      </div>

      {loading ? (
        <div className="loading-center">
          <div className="spinner-lg" />
        </div>
      ) : history.length === 0 ? (
        <div className="empty-state">
          <div className="empty-state-icon">
            <History size={30} />
          </div>
          <h3>No analyses yet</h3>
          <p>Upload a resume to get started</p>
          <Link to="/" className="btn btn-primary" style={{ marginTop: 20 }}>
            Analyze Your First Resume
          </Link>
        </div>
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>#</th>
                <th>File</th>
                <th>Job Role</th>
                <th>Score</th>
                <th>Date</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {history.map((item, idx) => {
                const cls = scoreClass(item.score)
                return (
                  <tr key={item.id}>
                    <td style={{ color: 'var(--text-muted)', fontWeight: 500 }}>{idx + 1}</td>
                    <td style={{ fontWeight: 500, maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                      {item.filename}
                    </td>
                    <td>
                      <span className="badge badge-blue">{item.jobRole}</span>
                    </td>
                    <td>
                      <div className="score-bar">
                        <div className="score-bar-track">
                          <div className={`score-bar-fill ${cls}`} style={{ width: `${item.score}%` }} />
                        </div>
                        <span className="score-text" style={{ color: scoreColor[cls] }}>
                          {item.score}%
                        </span>
                      </div>
                    </td>
                    <td style={{ color: 'var(--text-muted)', fontSize: 13 }}>
                      {new Date(item.createdAt).toLocaleDateString()}
                    </td>
                    <td>
                      <button
                        className="btn btn-outline"
                        style={{ padding: '6px 12px', fontSize: 13 }}
                        onClick={() => navigate(`/result/${item.id}`)}
                      >
                        <Eye size={13} /> View
                      </button>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
