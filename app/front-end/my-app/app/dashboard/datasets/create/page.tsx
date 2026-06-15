"use client";

import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { Plus, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { datasetModel, ICreateDatasetInput } from "@/app/api/dataset/datasetModel";
import { Spinner } from "@/components/ui/spinner";

export default function CreateDatasetPage() {
    const router = useRouter();
    const {
        handleSubmit,
        register,
        formState: { errors, isSubmitting },
    } = useForm<ICreateDatasetInput>();

    async function onSubmit(data: ICreateDatasetInput) {
        await datasetModel.createDataset(data);
        router.push("/dashboard");
    }

    return (
        <main className="flex flex-1 justify-center items-center">
            <Card className="w-125">
                <CardHeader>
                    <CardTitle>Novo Dataset</CardTitle>
                    <CardDescription>Preencha os dados do dataset</CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4">
                        <div className="grid gap-2">
                            <Label htmlFor="name">Nome *</Label>
                            <Input
                                id="name"
                                placeholder="Nome do dataset"
                                {...register("name", { required: "Informe um nome" })}
                            />
                            {errors.name && (
                                <span className="text-sm text-red-500">{errors.name.message}</span>
                            )}
                        </div>

                        <div className="grid gap-2">
                            <Label htmlFor="description">Descrição</Label>
                            <Input
                                id="description"
                                placeholder="Descrição do dataset"
                                {...register("description")}
                            />
                        </div>

                        <div className="grid gap-2">
                            <Label htmlFor="origin">Origem</Label>
                            <Input
                                id="origin"
                                placeholder="Origem dos dados"
                                {...register("origin")}
                            />
                        </div>

                        <div className="flex gap-2 justify-end">
                            <Button
                                type="button"
                                variant="outline"
                                onClick={() => router.push("/dashboard")}
                            >
                                <X className="size-4" />Cancelar
                            </Button>
                            <Button type="submit" disabled={isSubmitting}>
                                {isSubmitting ? <Spinner className="size-4" /> : <Plus className="size-4" />}
                                {isSubmitting ? "Salvando..." : "Criar Dataset"}
                            </Button>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </main>
    );
}
