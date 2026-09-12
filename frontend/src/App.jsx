import { useEffect, useState } from 'react';
import './App.css'

function App() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/dashboard/stats")
    .then(response => response.json())
    .then(data => {
      console.log(data);
      setStats(data);
    })
    .catch(errror => {
      console.error("Error loading dashboard: ", error);
    });

  }, []);

  return (
    <div>
      <h1>WhatsApp Sales Agent</h1>
      <h2>Sales Dashboard</h2>

      {stats ? (
        <div>
          <p>New Lead: {stats.newLeads}</p>
          <p>Open Escalations: {stats.openEscalations}</p>
          <p>Total Products: {stats.totalProducts}</p>
        </div>
      ):(
        <p>Loading dashboard...</p>
      )}
    </div>
  )
}

export default App
