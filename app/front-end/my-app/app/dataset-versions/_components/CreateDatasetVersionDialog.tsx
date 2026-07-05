"use client";

import { useState } from "react";
import { Controller, useFieldArray, useForm } from "react-hook-form";
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

type FeatureFormInput = {
    name: string;
    description?: string;
};

type PublishFormInput = {
    file: FileList;
    version: number;
    modifications?: string;
    parentDatasetVersionId?: string;
    features: FeatureFormInput[];
};

function parseCsvHeaderColumns(text: string): string[] {
    const firstLine = text.split(/\r?\n/, 1)[0] ?? "";

    return firstLine
        .split(",")
        .map((column) => column.trim().replace(/^"|"$/g, ""))
        .filter((column) => column.length > 0);
}

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
    const [featuresError, setFeaturesError] = useState<string | null>(null);

    const {
        handleSubmit,
        register,
        control,
        reset,
        formState: { errors, isSubmitting },
    } = useForm<PublishFormInput>({ defaultValues: { features: [] } });

    const { fields, replace } = useFieldArray({
        control,
        name: "features",
    });

    const fileRegister = register("file", { required: "Selecione um arquivo" });

    function handleOpenChange(open: boolean) {
        onOpenChange(open);
        if (!open) {
            reset();
            setFeaturesError(null);
        }
    }

    function handleFileChange(e: React.ChangeEvent<HTMLInputElement>) {
        const file = e.target.files?.[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = () => {
            const columns = parseCsvHeaderColumns(reader.result as string);
            if (columns.length > 0) {
                replace(columns.map((name) => ({ name, description: "" })));
                setFeaturesError(null);
            }
        };
        reader.readAsText(file);
    }

    async function onSubmit(data: PublishFormInput) {
        if (!data.features || data.features.length === 0) {
            setFeaturesError("Adicione ao menos uma feature do dataset");
            return;
        }
        setFeaturesError(null);

        await datasetVersionModel.createDatasetVersion({
            file: data.file[0],
            version: Number(data.version),
            datasetId,
            modifications: data.modifications || undefined,
            parentDatasetVersionId: data.parentDatasetVersionId || undefined,
            features: data.features.map((feature) => ({
                name: feature.name,
                description: feature.description || undefined,
            })),
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
                            accept=".csv"
                            {...fileRegister}
                            onChange={(e) => {
                                fileRegister.onChange(e);
                                handleFileChange(e);
                            }}
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

                    <div className="grid gap-2">
                        <Label>Features *</Label>
                        <p className="text-sm text-muted-foreground">
                            Detectadas automaticamente do cabeçalho do arquivo CSV. Ajuste os nomes
                            e, se quiser, adicione uma descrição para cada uma.
                        </p>

                        <div className="flex flex-col gap-2">
                            {fields.map((field, index) => (
                                <div key={field.id} className="flex items-start gap-2">
                                    <Input
                                        placeholder="Nome da feature"
                                        {...register(`features.${index}.name` as const, {
                                            required: "Informe o nome da feature",
                                        })}
                                    />
                                    <Input
                                        placeholder="Descrição (opcional)"
                                        {...register(`features.${index}.description` as const)}
                                    />
                                </div>
                            ))}
                        </div>

                        {errors.features?.map?.((featureError, index) =>
                            featureError?.name ? (
                                <span key={index} className="text-sm text-red-500">
                                    {featureError.name.message}
                                </span>
                            ) : null
                        )}
                        {featuresError && <span className="text-sm text-red-500">{featuresError}</span>}
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
