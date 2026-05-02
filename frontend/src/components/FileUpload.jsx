import { useCallback } from 'react'
import { useDropzone } from 'react-dropzone'
import { Upload, CheckCircle, File } from 'lucide-react'

export default function FileUpload({ onFileSelect, selectedFile }) {
  const onDrop = useCallback(
    (accepted) => { if (accepted.length > 0) onFileSelect(accepted[0]) },
    [onFileSelect]
  )

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'application/pdf': ['.pdf'],
      'application/msword': ['.doc'],
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document': ['.docx'],
    },
    maxFiles: 1,
    maxSize: 10 * 1024 * 1024,
  })

  const hasFile = Boolean(selectedFile)

  return (
    <div
      {...getRootProps()}
      className={`dropzone${isDragActive ? ' drag-active' : ''}${hasFile ? ' has-file' : ''}`}
    >
      <input {...getInputProps()} />

      {hasFile ? (
        <>
          <div className="dropzone-icon success">
            <CheckCircle size={26} />
          </div>
          <h3>{selectedFile.name}</h3>
          <p>{(selectedFile.size / 1024).toFixed(1)} KB · Click to replace</p>
        </>
      ) : (
        <>
          <div className="dropzone-icon">
            {isDragActive ? <File size={26} /> : <Upload size={26} />}
          </div>
          <h3>{isDragActive ? 'Drop your resume here' : 'Upload your resume'}</h3>
          <p>Drag &amp; drop or click to browse</p>
          <div className="file-types">
            <span className="file-type-badge">PDF</span>
            <span className="file-type-badge">DOC</span>
            <span className="file-type-badge">DOCX</span>
          </div>
          <p style={{ fontSize: 11, color: 'var(--gray-400)', marginTop: 4 }}>Max 10 MB</p>
        </>
      )}
    </div>
  )
}
