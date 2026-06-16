"use client";

import { Controller, useForm } from "react-hook-form";
import { Upload } from "lucide-react";
import { datasetVersionModel } from "@/app/api/dataset-version/datasetVersionModel";
import { IDatasetVersion } from "@/app/api/types";
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
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { Spinner } from "@/components/ui/spinner";

type PublishFormInput = {
    file: FileList;
    version: number;
    modifications?: string;
    parentDatasetVersionId?: string;
};

interface Props {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    onSuccess: () => void;
    datasetId: string;
    datasetName: string;
    existingVersions: IDatasetVersion[];
}

export function CreateDatasetVersionDialog({
    open,
    onOpenChange,
    onSuccess,
    datasetId,
    datasetName,
    existingVersions,
}: Props) {
    const {
        handleSubmit,
        register,
        control,
        reset,
        formState: { errors, isSubmitting },
    } = useForm<PublishFormInput>();

    function handleOpenChange(open: boolean) {
        onOpenChange(open);
        if (!open) reset();
    }

    async function onSubmit(data: PublishFormInput) {
        await datasetVersionModel.createDatasetVersion({
            file: data.file[0],
            version: Number(data.version),
            datasetId,
            modifications: data.modifications || undefined,
            parentDatasetVersionId: data.parentDatasetVersionId || undefined,
        });
        reset();
        onOpenChange(false);
        onSuccess();
    }

    return (
        <Dialog open={open} onOpenChange={handleOpenChange}>
            <DialogContent className="sm:max-w-md">
                <DialogHeader>
                    <DialogTitle>Nova versão</DialogTitle>
                    <DialogDescription>
                        Publique uma nova versão de {decodeURIComponent(datasetName ?? "")}
                    </DialogDescription>
                </DialogHeader>

                <form id="publish-form" onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4">
                    <div className="grid gap-2">
                        <Label htmlFor="file">Arquivo *</Label>
                        <Input
                            id="file"
                            type="file"
                            {...register("file", { required: "Selecione um arquivo" })}
                        />
                        {errors.file && <span className="text-sm text-red-500">{errors.file.message}</span>}
                    </div>

                    <div className="grid gap-2">
                        <Label>Versão pai</Label>
                        <Controller
                            name="parentDatasetVersionId"
                            control={control}
                            render={({ field }) => (
                                <Select value={field.value} onValueChange={field.onChange}>
                                    <SelectTrigger className="w-full">
                                        <SelectValue placeholder="Nenhuma (versão raiz)" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        {existingVersions.map((v) => (
                                            <SelectItem key={v.id} value={v.id}>
                                                v{v.version}{v.modifications ? ` — ${v.modifications}` : ""}
                                            </SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                            )}
                        />
                    </div>

                    <div className="grid gap-2">
                        <Label htmlFor="version">Versão *</Label>
                        <Input
                            id="version"
                            type="number"
                            min={1}
                            placeholder="ex: 1"
                            {...register("version", {
                                required: "Informe a versão",
                                min: { value: 1, message: "Informe uma versão válida" },
                            })}
                        />
                        {errors.version && <span className="text-sm text-red-500">{errors.version.message}</span>}
                    </div>

                    <div className="grid gap-2">
                        <Label htmlFor="modifications">Modificações</Label>
                        <Input
                            id="modifications"
                            placeholder="Descreva as modificações desta versão"
                            {...register("modifications")}
                        />
                    </div>
                </form>

                <DialogFooter>
                    <Button type="submit" form="publish-form" disabled={isSubmitting}>
                        {isSubmitting ? <Spinner className="size-4" /> : <Upload className="size-4" />}
                        {isSubmitting ? "Publicando..." : "Publicar versão"}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
