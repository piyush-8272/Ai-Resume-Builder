import { CheckCircle, XCircle } from 'lucide-react'

export default function SkillTag({ skill, type }) {
  return (
    <span className={`skill-tag ${type}`}>
      {type === 'found'
        ? <CheckCircle size={11} />
        : <XCircle size={11} />}
      {skill}
    </span>
  )
}
