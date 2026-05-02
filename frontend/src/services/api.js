import axios from 'axios'

const http = axios.create({ baseURL: '/api' })

export async function uploadResume(file) {
  const form = new FormData()
  form.append('file', file)
  const { data } = await http.post('/upload-resume', form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  return data
}

export async function analyzeResume(resumeText, jobRole, filename) {
  const { data } = await http.post('/analyze', { resumeText, jobRole, filename })
  return data
}

export async function getResult(id) {
  const { data } = await http.get(`/results/${id}`)
  return data
}

export async function getHistory() {
  const { data } = await http.get('/history')
  return data
}

export async function getJobRoles() {
  const { data } = await http.get('/job-roles')
  return data
}
