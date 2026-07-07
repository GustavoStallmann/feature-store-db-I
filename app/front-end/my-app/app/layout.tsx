import type { Metadata } from "next";
import { Poppins } from "next/font/google";
import { Toaster } from "@/components/ui/sonner";
import "./globals.css";

const poppins = Poppins({
  variable: "--font-poppins",
  weight: '500',
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Feature store",
  description: "",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="pt-br"
      suppressHydrationWarning
      className={`${poppins.className} dark h-full antialiased`}
    >
      <body suppressHydrationWarning className="min-h-full flex flex-col px-6">
        {children}
        <Toaster theme="dark" />
      </body>
    </html>
  );
}
