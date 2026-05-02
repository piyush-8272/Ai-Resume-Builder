export default function SuggestionsList({ suggestions }) {
  if (!suggestions?.length) return null

  return (
    <ul className="suggestions-list">
      {suggestions.map((text, i) => (
        <li key={i} className="suggestion-item">
          <div className="suggestion-number">{i + 1}</div>
          <p className="suggestion-text">{text}</p>
        </li>
      ))}
    </ul>
  )
}
