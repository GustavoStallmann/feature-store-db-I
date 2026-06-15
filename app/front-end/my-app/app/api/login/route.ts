import { NextResponse } from 'next/server';
import { cookies } from 'next/headers';

export async function POST(request: Request) {
  try {
    const { cpf } = await request.json();

    // 1. Forward credentials to your Spring Boot backend
    const springResponse = await fetch('http://localhost:8080/api/users/cpf?cpf=${cpf}' );

    // If Spring Boot says the user doesn't exist or password is wrong
    if (!springResponse.ok) {
      return NextResponse.json(
        { message: 'Invalid user cpf' },
        { status: 401 }
      );
    }

    // Assuming Spring Boot returns a JSON object like: { token: "XYZ_JWT_TOKEN" }
    const data = await springResponse.json();
    const token = data.token;

    // 2. Save the token into a secure HTTP-only cookie
    const cookieStore = await cookies();
    cookieStore.set('auth_token', token, {
      httpOnly: true,       // Prevents client-side JS from reading the cookie (XSS protection)
      secure: process.env.NODE_ENV === 'production', // Only sends over HTTPS in production
      sameSite: 'strict',   // Protects against CSRF attacks
      maxAge: 60 * 60 * 24, // Cookie expires in 1 day
      path: '/',
    });

    return NextResponse.json({ success: true });

  } catch (error) {
    console.error('Login error:', error);
    return NextResponse.json(
      { message: 'Internal server error' },
      { status: 500 }
    );
  }
}
