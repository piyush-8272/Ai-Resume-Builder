const RADIUS = 72
const CIRC = 2 * Math.PI * RADIUS

function gradeInfo(score) {
  if (score >= 80) return { cls: 'excellent', label: 'Excellent Match', color: '#10b981' }
  if (score >= 60) return { cls: 'good',      label: 'Good Match',      color: '#3b82f6' }
  if (score >= 40) return { cls: 'average',   label: 'Average Match',   color: '#f59e0b' }
  return              { cls: 'poor',      label: 'Needs Improvement', color: '#ef4444' }
}

export default function ScoreCard({ score }) {
  const { cls, label, color } = gradeInfo(score)
  const offset = CIRC - (score / 100) * CIRC

  return (
    <div className="score-wrapper">
      <div className="score-circle">
        <svg width="180" height="180" viewBox="0 0 180 180">
          <circle cx="90" cy="90" r={RADIUS} fill="none" stroke="#e2e8f0" strokeWidth="12" />
          <circle
            cx="90" cy="90" r={RADIUS}
            fill="none"
            stroke={color}
            strokeWidth="12"
            strokeDasharray={CIRC}
            strokeDashoffset={offset}
            strokeLinecap="round"
            style={{ transition: 'stroke-dashoffset 1.1s ease' }}
          />
        </svg>
        <div className="score-circle-text">
          <span className="score-value">{score}%</span>
          <span className="score-label">Match Score</span>
        </div>
      </div>
      <span className={`score-grade ${cls}`}>{label}</span>
    </div>
  )
}
