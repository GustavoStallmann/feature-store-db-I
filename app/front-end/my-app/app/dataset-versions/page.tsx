"use client";

import { IDatasetVersion } from "@/app/api/types";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Spinner } from "@/components/ui/spinner";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { ArrowLeft, Download, Eye, Plus } from "lucide-react";
import { useRouter } from "next/dist/client/components/navigation";
import { use, useEffect, useState } from "react";
import { datasetVersionDownloadModel } from "../api/dataset-version-download/datasetVersionDownloadModel";
import { datasetVersionModel } from "../api/dataset-version/datasetVersionModel";
import { CreateDatasetVersionDialog } from "./_components/CreateDatasetVersionDialog";
import { VersionTreeView } from "./_components/VersionTreeView";

export default function DatasetVersionsPage({
  searchParams,
}: {
  searchParams: Promise<{ datasetId: string; datasetName: string }>;
}) {
  const { datasetId, datasetName } = use(searchParams);
  const router = useRouter();

  const [datasetVersions, setDatasetVersions] = useState<IDatasetVersion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    datasetVersionModel
      .listByDatasetId(datasetId)
      .then((res) => setDatasetVersions(res.data))
      .catch(() => setError("Falha ao carregar versões do dataset."))
      .finally(() => setLoading(false));
  }, [datasetId, refreshKey]);

  const refresh = () => {
    setLoading(true);
    setRefreshKey((k) => k + 1);
  };

  const handleDownload = async (datasetVersionId: string) => {
    try {
      const blob = await datasetVersionDownloadModel.downloadDatasetVersion(datasetVersionId);
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "dataset.csv";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Download failed:", err);
    }
  };

  const goToFeatures = (datasetVersion: IDatasetVersion) => {
    router.push(
      `/dataset-version-features?datasetId=${datasetId}&datasetName=${encodeURIComponent(datasetName)}&datasetVersionId=${datasetVersion.id}&datasetVersionName=${encodeURIComponent(datasetVersion.version.toString())}`
    );
  };

  return (
    <main style={{ padding: "20px" }}>
      <CreateDatasetVersionDialog
        open={dialogOpen}
        onOpenChange={setDialogOpen}
        onSuccess={refresh}
        datasetId={datasetId}
        datasetName={datasetName}
        existingVersions={datasetVersions}
      />

      <Card>
        <CardHeader>
          <CardTitle>{decodeURIComponent(datasetName ?? "Versões do dataset")}</CardTitle>
          <CardAction className="flex gap-2">
            <Button onClick={() => setDialogOpen(true)}>
              <Plus className="size-4" />Adicionar nova versão
            </Button>
            <Button variant="outline" onClick={() => router.push("/dashboard")}>
              <ArrowLeft className="size-4" />Voltar ao dashboard
            </Button>
          </CardAction>
        </CardHeader>
        <CardContent>
          {error && <p style={{ color: "red" }}>{error}</p>}

          {!error && loading && (
            <div className="flex justify-center py-6">
              <Spinner className="size-5" />
            </div>
          )}

          {!error && !loading && (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Criada em</TableHead>
                  <TableHead>Versão</TableHead>
                  <TableHead>Modificações</TableHead>
                  <TableHead>Criada por</TableHead>
                  <TableHead>Versão Pai</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {datasetVersions.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={5} className="text-center text-muted-foreground">
                      Nenhuma versão desse dataset encontrada.
                    </TableCell>
                  </TableRow>
                ) : (
                  datasetVersions.map((datasetVersion) => (
                    <TableRow key={datasetVersion.id}>
                      <TableCell>{new Date(datasetVersion.createdAt).toLocaleDateString("pt-BR")}</TableCell>
                      <TableCell>{datasetVersion.version}</TableCell>
                      <TableCell>{datasetVersion.modifications ?? "—"}</TableCell>
                      <TableCell>{datasetVersion.submittingUser.name}</TableCell>
                      <TableCell>{datasetVersion.parentDatasetVersion ? datasetVersion.parentDatasetVersion.version : "—"}</TableCell>
                      <TableCell className="flex gap-2">
                        <Button variant="outline" size="sm" onClick={() => handleDownload(datasetVersion.id)}>
                          <Download className="size-4" />Download
                        </Button>
                        <Button variant="outline" size="sm" onClick={() => goToFeatures(datasetVersion)}>
                          <Eye className="size-4" />Detalhes das Features
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          )}

          {!error && !loading && datasetVersions.length > 0 && (
            <div className="mt-6">
              <h3 className="mb-2 text-sm font-semibold text-muted-foreground">Árvore de versões</h3>
              <VersionTreeView datasetVersions={datasetVersions} />
            </div>
          )}
        </CardContent>
      </Card>
    </main>
  );
}
