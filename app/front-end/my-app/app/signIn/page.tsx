"use client";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { Button } from "../../components/ui/button";
import {
     Card,
     CardContent,
     CardDescription,
     CardHeader,
     CardTitle
} from "../../components/ui/card";
import { Input } from "../../components/ui/input";
import { Label } from "../../components/ui/label";
import { authModel } from '../api/auth/authModel';
import { IAuthSignInInput } from "../api/types";

export default function SignIn() {
     const router = useRouter();
     const {
          handleSubmit,
          register
     } = useForm<IAuthSignInInput>()

     async function onSubmit(data: IAuthSignInInput) {
          await authModel.signIn(data);
          router.push('/dashboard');
     }

     return (
          <main className="flex flex-1 justify-center items-center">
               <Card className="w-125">
                    <CardHeader>
                         <CardTitle>Acesse sua conta</CardTitle>
                         <CardDescription>
                              Preencha as credenciais abaixo
                         </CardDescription>
                    </CardHeader>
                    <CardContent>
                         <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-3">
                              <div className="flex flex-col gap-6">
                                   <div className="grid gap-2">
                                        <Label htmlFor="cpf">Cpf</Label>
                                        <Input
                                             id="cpf"
                                             type="text"
                                             placeholder="000.000.000-00"
                                             {...register('cpf', { required: true })}
                                        />
                                   </div>
                              </div>
                              <div className="flex flex-col gap-6">
                                   <div className="grid gap-2">
                                        <Label htmlFor="senha">Senha</Label>
                                        <Input
                                             id="senha"
                                             type="password"
                                             placeholder="••••••••"
                                             {...register('password', { required: true })}
                                             required
                                        />
                                   </div>
                              </div>
                              <Button type="submit" className="w-full">
                                   Login
                              </Button>
                         </form>
                    </CardContent>
               </Card>
          </main>
     );
}
