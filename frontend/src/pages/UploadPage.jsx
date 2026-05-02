import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { Briefcase, ArrowRight, FileSearch } from 'lucide-react'
import FileUpload from '../components/FileUpload'
import { uploadResume, analyzeResume, getJobRoles } from '../services/api'

export default function UploadPage() {
  const navigate = useNavigate()

  const [file, setFile]               = useState(null)
  const [jobRole, setJobRole]         = useState('')
  const [jobRoles, setJobRoles]       = useState([])
  const [uploading, setUploading]     = useState(false)
  const [analyzing, setAnalyzing]     = useState(false)
  const [uploaded, setUploaded]       = useState(null)   // { filename, extractedText, extractedTextPreview }

  useEffect(() => {
    getJobRoles()
      .then(setJobRoles)
      .catch(() => toast.error('Failed to load job roles'))
  }, [])

  async function handleFileSelect(selected) {
    setFile(selected)
    setUploaded(null)
    setUploading(true)

    try {
      const data = await uploadResume(selected)
      setUploaded(data)
      toast.success('Resume uploaded successfully!')
    } catch {
      toast.error('Upload failed. Please try again.')
      setFile(null)
    } finally {
      setUploading(false)
    }
  }

  async function handleAnalyze() {
    if (!uploaded)  { toast.error('Please upload a resume first.');   return }
    if (!jobRole)   { toast.error('Please select a job role.');        return }

    setAnalyzing(true)
    try {
      const result = await analyzeResume(uploaded.extractedText, jobRole, uploaded.filename)
      toast.success('Analysis complete!')
      navigate(`/result/${result.id}`)
    } catch {
      toast.error('Analysis failed. Please try again.')
    } finally {
      setAnalyzing(false)
    }
  }

  const canAnalyze = uploaded && jobRole && !uploading && !analyzing

  return (
    <div className="animate-in">
      {/* Hero */}
      <div className="upload-hero">
        <span className="hero-badge">
          <FileSearch size={13} />
          AI-Powered Skill Matching
        </span>
        <h1>Analyze Your Resume</h1>
        <p>
          Upload your resume and pick a target role to get an instant match score,
          skill gap report, and tailored improvement tips.
        </p>
      </div>

      {/* Two-column form */}
      <div className="upload-grid">
        {/* Left: Upload */}
        <div className="card">
          <div className="card-header">
            <div className="card-icon blue"><FileSearch size={17} /></div>
            <h2 className="card-title">Upload Resume</h2>
          </div>

          <FileUpload onFileSelect={handleFileSelect} selectedFile={file} />

          {uploading && (
            <p style={{ textAlign: 'center', marginTop: 12, fontSize: 13, color: 'var(--text-muted)' }}>
              Extracting text…
            </p>
          )}

          {uploaded && (
            <div style={{ marginTop: 16 }}>
              <p style={{ fontSize: 12, fontWeight: 600, color: 'var(--gray-600)', marginBottom: 6 }}>
                Extracted text preview
              </p>
              <div className="text-preview">{uploaded.extractedTextPreview}</div>
            </div>
          )}
        </div>

        {/* Right: Job role + submit */}
        <div className="card">
          <div className="card-header">
            <div className="card-icon blue"><Briefcase size={17} /></div>
            <h2 className="card-title">Select Job Role</h2>
          </div>

          <div className="form-group">
            <label className="form-label">Target Role</label>
            <select
              className="form-select"
              value={jobRole}
              onChange={e => setJobRole(e.target.value)}
            >
              <option value="">— Choose a role —</option>
              {jobRoles.map(r => (
                <option key={r} value={r}>{r}</option>
              ))}
            </select>
          </div>

          {jobRole && (
            <div style={{
              padding: '10px 14px',
              background: 'var(--primary-50)',
              borderRadius: 'var(--radius)',
              marginBottom: 16,
              fontSize: 13,
              color: 'var(--primary-600)',
              fontWeight: 500,
            }}>
              Analyzing for <strong>{jobRole}</strong>
              {!uploaded && (
                <span style={{ color: 'var(--text-muted)', fontWeight: 400 }}>
                  {' '}· Upload a resume to continue
                </span>
              )}
            </div>
          )}

          <button
            className="btn btn-primary btn-full btn-lg"
            onClick={handleAnalyze}
            disabled={!canAnalyze}
          >
            {analyzing
              ? <><span className="spinner" /> Analyzing…</>
              : <>Analyze Resume <ArrowRight size={16} /></>}
          </button>

          {/* Feature list */}
          <div style={{
            marginTop: 20, padding: 16,
            background: 'var(--gray-50)',
            borderRadius: 'var(--radius)',
          }}>
            <p style={{ fontSize: 12, fontWeight: 600, color: 'var(--gray-600)', marginBottom: 10 }}>
              What you&apos;ll get
            </p>
            {[
              'Detected skills from your resume',
              'Match score vs. role requirements',
              'Missing skills to add',
              'Personalized improvement tips',
            ].map(item => (
              <div key={item} style={{ display: 'flex', gap: 8, alignItems: 'center', marginBottom: 6 }}>
                <span style={{ color: 'var(--success-500)', fontSize: 14, lineHeight: 1 }}>✓</span>
                <span style={{ fontSize: 13, color: 'var(--text-muted)' }}>{item}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
