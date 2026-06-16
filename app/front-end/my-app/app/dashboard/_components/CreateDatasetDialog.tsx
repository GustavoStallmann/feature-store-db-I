"use client";

import { useForm } from "react-hook-form";
import { Plus } from "lucide-react";
import { datasetModel, ICreateDatasetInput } from "@/app/api/dataset/datasetModel";
import { Button } from "@/components/ui/button";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Spinner } from "@/components/ui/spinner";

interface Props {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    onSuccess: () => void;
}

export function CreateDatasetDialog({ open, onOpenChange, onSuccess }: Props) {
    const {
        handleSubmit,
        register,
        reset,
        formState: { errors, isSubmitting },
    } = useForm<ICreateDatasetInput>();

    function handleOpenChange(open: boolean) {
        onOpenChange(open);
        if (!open) reset();
    }

    async function onSubmit(data: ICreateDatasetInput) {
        await datasetModel.createDataset(data);
        reset();
        onOpenChange(false);
        onSuccess();
    }

    return (
        <Dialog open={open} onOpenChange={handleOpenChange}>
            <DialogContent className="sm:max-w-md">
                <DialogHeader>
                    <DialogTitle>Novo Dataset</DialogTitle>
                    <DialogDescription>Preencha os dados do dataset</DialogDescription>
                </DialogHeader>

                <form id="create-dataset-form" onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4">
                    <div className="grid gap-2">
                        <Label htmlFor="name">Nome *</Label>
                        <Input
                            id="name"
                            placeholder="Nome do dataset"
                            {...register("name", { required: "Informe um nome" })}
                        />
                        {errors.name && <span className="text-sm text-red-500">{errors.name.message}</span>}
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
                </form>

                <DialogFooter>
                    <Button type="submit" form="create-dataset-form" disabled={isSubmitting}>
                        {isSubmitting ? <Spinner className="size-4" /> : <Plus className="size-4" />}
                        {isSubmitting ? "Salvando..." : "Criar Dataset"}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
