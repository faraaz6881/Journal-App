import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { fetchApi } from '../utils/api';
import { ArrowLeft, Save, Sparkles } from 'lucide-react';

export default function EntryDetails() {
  const { id } = useParams();
  const isNew = id === 'new';
  const navigate = useNavigate();
  const { user } = useAuth();
  
  const [formData, setFormData] = useState({ title: '', content: '' });
  const [loading, setLoading] = useState(false);
  const [grammarLoading, setGrammarLoading] = useState(false);
  const [error, setError] = useState('');
  const [grammarResult, setGrammarResult] = useState('');

  useEffect(() => {
    if (!isNew) {
      fetchEntry();
    }
  }, [id]);

  const fetchEntry = async () => {
    try {
      const data = await fetchApi(`/journal/id/${id}`);
      setFormData({ title: data.title || '', content: data.content || '' });
    } catch (err) {
      setError('Failed to fetch entry details.');
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      if (isNew) {
        await fetchApi(`/journal?username=${user.username}`, {
          method: 'POST',
          body: JSON.stringify(formData),
        });
      } else {
        await fetchApi(`/journal/id/${id}`, {
          method: 'PUT',
          body: JSON.stringify(formData),
        });
      }
      navigate('/dashboard');
    } catch (err) {
      setError('Failed to save entry.');
    } finally {
      setLoading(false);
    }
  };

  const handleGrammarCheck = async () => {
    if (!formData.content) {
      setError("Please write some content first!");
      return;
    }
    setGrammarLoading(true);
    setGrammarResult('');
    setError('');
    
    try {
      let data;
      if (isNew) {
        data = await fetchApi('/journal/grammar-check', {
          method: 'POST',
          body: JSON.stringify({ text: formData.content }),
        });
      } else {
        data = await fetchApi(`/journal/id/${id}/grammar-check`);
      }
      setGrammarResult(data);
    } catch (err) {
      setError("Grammar check failed. Try saving first if editing, or check if the service is up.");
    } finally {
      setGrammarLoading(false);
    }
  };

  return (
    <div className="app-container">
      <div style={{ marginBottom: '24px' }}>
        <button onClick={() => navigate('/dashboard')} className="btn btn-secondary" style={{ padding: '8px 16px', border: 'none' }}>
          <ArrowLeft size={20} /> Back to Dashboard
        </button>
      </div>

      <div className="glass-panel" style={{ maxWidth: '800px', margin: '0 auto' }}>
        <h1 className="page-title">{isNew ? 'New Entry' : 'Edit Entry'}</h1>
        
        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSave}>
          <input
            type="text"
            name="title"
            placeholder="Give it a title..."
            value={formData.title}
            onChange={handleChange}
            className="input-field"
            style={{ fontSize: '1.5rem', fontWeight: 'bold' }}
            required
          />

          <textarea
            name="content"
            placeholder="What's on your mind?"
            value={formData.content}
            onChange={handleChange}
            className="input-field"
            style={{ minHeight: '300px', resize: 'vertical', fontFamily: 'inherit' }}
            required
          />

          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '24px' }}>
            <button 
              type="button" 
              onClick={handleGrammarCheck} 
              className="btn" 
              style={{ backgroundColor: 'rgba(139, 92, 246, 0.2)', color: '#a78bfa', border: '1px solid currentColor' }}
              disabled={grammarLoading}
            >
              <Sparkles size={20} />
              {grammarLoading ? 'Checking...' : 'Check Grammar'}
            </button>

            <button type="submit" className="btn btn-primary" disabled={loading}>
              <Save size={20} />
              {loading ? 'Saving...' : 'Save Entry'}
            </button>
          </div>
        </form>

        {grammarResult && (
          <div style={{ marginTop: '32px', padding: '16px', backgroundColor: 'var(--bg-secondary)', borderRadius: '8px', borderLeft: '4px solid #a78bfa' }}>
            <h3 style={{ marginBottom: '8px', display: 'flex', alignItems: 'center', gap: '8px', color: '#a78bfa' }}>
              <Sparkles size={16} /> AI Suggestions
            </h3>
            <p style={{ whiteSpace: 'pre-wrap', color: 'var(--text-secondary)' }}>{grammarResult}</p>
          </div>
        )}
      </div>
    </div>
  );
}
