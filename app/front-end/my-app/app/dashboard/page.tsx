// app/dashboard/page.tsx
import { cookies } from 'next/headers';
import { redirect } from 'next/navigation';

export default async function DashboardPage() {
  const cookieStore = await cookies();
  const token = cookieStore.get('auth_token')?.value;

  // If no cookie exists, kick them back to login page
  if (!token) {
    redirect('/login');
  }

  // Fetch from Spring Boot using the secure token
  const response = await fetch('http://localhost:8080/api/dataset/get', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    return <p>Error loading dashboard data.</p>;
  }

  const data = await response.json();

  return (
    <main style={{ padding: '20px' }}>
      <h1>Welcome to your Dashboard</h1>
      <p>Data from database: {JSON.stringify(data)}</p>
    </main>
  );
}
