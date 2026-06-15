"use client";
import { Button } from "../components/ui/button"
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../components/ui/card"
import { Input } from "../components/ui/input"
import { Label } from "../components/ui/label"
import { useRouter } from 'next/navigation';
import { useState, FormEvent } from 'react';
export default function Home() { 
     const [cpf, setCpf] = useState('');
     const [error, setError] = useState('');
     const [loading, setLoading] = useState(false);
     const router = useRouter();

     const handleSubmit = async (e: FormEvent) => {
          e.preventDefault();
          setError('');
          setLoading(true);

          try {
               const res = await fetch('/api/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ cpf}),
               });

               if (!res.ok) {
                    const data = await res.json();
                    throw new Error(data.message || 'Invalid credentials');
               }

               // Login successful! Redirect to a protected route
               router.push('/dashboard');
               router.refresh();
          } catch (err: any) {
               setError(err.message);
          } finally {
               setLoading(false);
          }
     };

     return (
          <Card className="w-full max-w-sm">
               <form onSubmit={handleSubmit}>
               <CardHeader>
                    <CardTitle>Login to your account</CardTitle>
                    <CardDescription>
                    Enter your cpf below to login to your account
                    </CardDescription>
                    <CardAction>
                    <Button variant="link">Sign Up</Button>
                    </CardAction>
               </CardHeader>
               <CardContent>
                    <div className="flex flex-col gap-6">
                         <div className="grid gap-2">
                              <Label htmlFor="cpf">Cpf</Label>
                              <Input
                               id="cpf"
                               type="text"
                               placeholder="000.000.000-00"
                               required
                               value={cpf}
                               onChange={(e) => setCpf(e.target.value)}
                             />
                         </div>
                    </div>
               </CardContent>
               <CardFooter className="flex-col gap-2">
                    <Button type="submit" className="w-full">
                    Login
                    </Button>
               </CardFooter>
               </form>
          </Card>
     );
}
