import { useEffect, useState } from 'react';
import './App.css'

function App() {
  const [stats, setStats] = useState({
    newLeads: 0,
    operEscalations: 0,
    totalProducts: 0
  });

  const [leads, setLeads] = useState([]);
  const [escalations, setEscalations] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadDashboard(true);
    const interval = setInterval(() => {
      loadDashboard(false);
    }, 5000);

    return () => {
      clearInterval(interval);
    };
  }, []);

  async function loadDashboard(showLoading = false) {
    try {
      if (showLoading) {
        setLoading(true);
      }
      setError("");

      const [statsResponse, leadsResponse, escalationsResponse] =
        await Promise.all([
          fetch("http://localhost:8080/api/dashboard/stats"),
          fetch("http://localhost:8080/api/dashboard/leads"),
          fetch("http://localhost:8080/api/dashboard/escalations"),
        ]);

      if (!statsResponse.ok || !leadsResponse.ok || !escalationsResponse.ok) {
        throw new Error("Unable to load dashboard data.");
      }

      const statsData = await statsResponse.json();
      const leadsData = await leadsResponse.json();
      const escalationsData = await escalationsResponse.json();

      setStats(statsData);
      setLeads(leadsData);
      setEscalations(escalationsData);
    } catch (err) {
      console.error(err);
      setError("Unable to connect to the sales dashboard API.");
    } finally {
      if (showLoading) {
        setLoading(false);
      }
    }
  }

  if (loading) {
    return <p>Loading dashboard...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  async function markLeadContacted(id) {
    try {
      const response = await fetch(
        `http://localhost:8080/api/dashboard/leads/${id}/contacted`,
        {
          method: "PUT",
        },
      );

      if (!response.ok) {
        throw new Error("Unable to update sales lead.");
      }
      await loadDashboard();
    } catch (err) {
      console.error(err);
      alert("Unable to update sales lead");
    }
  }

  async function resolveEscalation(id) {
    try {
      const response = await fetch(
        `http://localhost:8080/api/dashboard/escalations/${id}/resolve`,
        {
          method: "PUT",
        },
      );

      if (!response.ok) {
        throw new Error("Unable to resolve escalation.");
      }

      await loadDashboard();
    } catch (err) {
      console.error(err);
      alert("Unable to resolve escalation.");
    }
  }

  return (
    <div className="dashboard">
      <header className="header">
        <div>
          <h1>WhatsApp Sales Agent</h1>
          <p>Sales Enquiry Command Centre</p>
        </div>

        <div className="agent-status">
          <span className="status-dot"></span>
          Agent Online
        </div>
      </header>
      <main className="content">
        <section className="stats-grid">
          <StatCard value={stats.newLeads} title="New Sales Leads" />
          <StatCard value={stats.openEscalations} title="Open Escalations" />
          <StatCard value={stats.totalProducts} title="Products" />
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <h2>Sales Leads</h2>
              <p>AI-detected purchase opportunities and follow-up status</p>
            </div>
            <span className="count-badge">{leads.length}</span>
          </div>
          <table>
            <thead>
              <tr>
                <th>Customer</th>
                <th>Phone</th>
                <th>Product</th>
                <th>Status</th>
                <th>Created</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {leads.map((lead) => (
                <tr key={lead.id}>
                  <td>
                    <strong>{lead.customerName}</strong>
                  </td>
                  <td>{lead.phoneNumber}</td>
                  <td>{lead.productName}</td>
                  <td>
                    <span className={"badge " + lead.status.toLowerCase()}>
                      {lead.status}
                    </span>
                  </td>
                  <td>{formatDate(lead.createdAt)}</td>
                  <td>
                    {lead.status === "NEW" ? (
                      <button
                        className="action-button"
                        onClick={() => markLeadContacted(lead.id)}
                      >
                        Mark Contacted
                      </button>
                    ) : (
                      <span>-</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <h2>Needs Human Attention</h2>
              <p>Enquiries escalated by the AI agent</p>
            </div>

            <span className="count-badge">{escalations.length}</span>
          </div>
          <table>
            <thead>
              <tr>
                <th>Customer</th>
                <th>Issue</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Created</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {escalations.map((item) => (
                <tr key={item.id}>
                  <td>
                    <strong>{item.customerName}</strong>
                  </td>
                  <td>{item.reason}</td>
                  <td>
                    <span className={"badge " + item.priority.toLowerCase()}>
                      {item.priority}
                    </span>
                  </td>
                  <td>
                    <span className={"badge " + item.status.toLowerCase()}>
                      {item.status}
                    </span>
                  </td>
                  <td>{formatDate(item.createdAt)}</td>
                  <td>
                    {item.status === "OPEN" ? (
                      <button
                        className="resolve-button"
                        onClick={() => resolveEscalation(item.id)}
                      >
                        Resolve
                      </button>
                    ) : (
                      <span>-</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      </main>
    </div>
  );
}

function StatCard({value, title}) {
  return(
    <div className="stat-card">
      <div className="stat-value">{value}</div>
      <div className="stat-title">{title}</div>
    </div>
  );
}

function formatDate(date) {
  if(!date) {
    return "-";
  }

  return new Date(date).toLocaleString();
}

export default App
