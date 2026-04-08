import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { fetchApi } from '../utils/api';
import { Plus, Edit3, Trash2 } from 'lucide-react';

export default function Dashboard() {
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    fetchEntries();
  }, [user]);

  const fetchEntries = async () => {
    try {
      const data = await fetchApi(`/journal/${user.username}`);
      setEntries(data || []);
    } catch (err) {
      if (err.message !== 'Unauthorized' && !err.message.includes('NOT_FOUND')) {
         setError('Failed to load entries');
      } else {
         setEntries([]);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    try {
      await fetchApi('/auth/logout', { method: 'POST' });
    } catch (err) {
      console.error(err);
    }
    logout();
    navigate('/');
  };

  const deleteEntry = async (id, e) => {
    e.stopPropagation();
    if (!window.confirm("Are you sure you want to delete this entry?")) return;
    
    try {
      await fetchApi(`/journal/id/${id}?username=${user.username}`, { method: 'DELETE' });
      setEntries(entries.filter(entry => entry.id !== id));
    } catch (err) {
      alert("Failed to delete entry");
    }
  };

  return (
    <div className="app-container min-h-screen" style={{ padding: 0 }}>
      <nav className="nav-bar">
        <div className="nav-brand">Journal App</div>
        <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
          <span style={{ color: 'var(--text-secondary)' }}>Hi, {user?.username}</span>
          <button onClick={handleLogout} className="btn btn-secondary" style={{ padding: '8px 16px' }}>
            Logout
          </button>
        </div>
      </nav>

      <main style={{ padding: '32px 24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
          <div>
            <h1 className="page-title">Your Journal</h1>
            <p className="page-subtitle" style={{ marginBottom: 0 }}>A safe place for our thoughts.</p>
          </div>
          <button onClick={() => navigate('/entry/new')} className="btn btn-primary">
            <Plus size={20} /> New Entry
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        {loading ? (
          <p>Loading your thoughts...</p>
        ) : entries.length === 0 ? (
          <div className="glass-panel flex-center" style={{ flexDirection: 'column', padding: '64px 24px', textAlign: 'center' }}>
            <Edit3 size={48} style={{ color: 'var(--text-secondary)', marginBottom: '16px' }} />
            <h2 style={{ fontSize: '1.5rem', marginBottom: '8px' }}>No entries yet</h2>
            <p style={{ color: 'var(--text-secondary)' }}>Create your first journal entry to get started.</p>
          </div>
        ) : (
          <div className="card-grid">
            {entries.map(entry => (
              <div 
                key={entry.id} 
                className="glass-panel journal-card"
                onClick={() => navigate(`/entry/${entry.id}`)}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <h3>{entry.title}</h3>
                  <button 
                    onClick={(e) => deleteEntry(entry.id, e)}
                    style={{ color: 'var(--text-secondary)', padding: '4px' }}
                    title="Delete entry"
                    onMouseOver={(e) => e.currentTarget.style.color = 'var(--danger-color)'}
                    onMouseOut={(e) => e.currentTarget.style.color = 'var(--text-secondary)'}
                  >
                    <Trash2 size={18} />
                  </button>
                </div>
                <p>{entry.content}</p>
                <div style={{ marginTop: '16px', fontSize: '0.8rem', color: 'var(--text-secondary)', alignSelf: 'flex-end' }}>
                   {new Date(entry.date).toLocaleDateString()}
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}
