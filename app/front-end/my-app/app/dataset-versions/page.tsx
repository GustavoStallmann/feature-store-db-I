"use client";

import { use, useEffect, useState } from "react";
import { useRouter } from "next/dist/client/components/navigation";
import { datasetVersionModel } from "../api/dataset-version/datasetVersionModel";
import { datasetVersionDownloadModel } from "../api/dataset-version-download/datasetVersionDownloadModel";
import { IDatasetVersion } from "@/app/api/types";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { ArrowLeft, Download } from "lucide-react";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

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

  useEffect(() => {
    datasetVersionModel
      .listByDatasetId(datasetId)
      .then((res) => setDatasetVersions(res.data))
      .catch(() => setError("Falha ao carregar versões do dataset."))
      .finally(() => setLoading(false));
  }, [datasetId]);

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

  return (
    <main style={{ padding: "20px" }}>
      <Card>
        <CardHeader>
          <CardTitle>{decodeURIComponent(datasetName ?? "Versões do dataset")}</CardTitle>
          <CardAction>
            <Button variant="outline" onClick={() => router.push("/dashboard")}>
              <ArrowLeft className="size-4" />Voltar ao dashboard
            </Button>
          </CardAction>
        </CardHeader>
        <CardContent>
          {error && <p style={{ color: "red" }}>{error}</p>}

          {!error && (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Versão</TableHead>
                  <TableHead>Modificações</TableHead>
                  <TableHead>Path</TableHead>
                  <TableHead>Criada por</TableHead>
                  <TableHead>Criada em</TableHead>
                  <TableHead />
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading ? (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center py-6">
                      <Spinner className="mx-auto size-5" />
                    </TableCell>
                  </TableRow>
                ) : datasetVersions.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center text-muted-foreground">
                      Nenhuma versão desse dataset encontrada.
                    </TableCell>
                  </TableRow>
                ) : (
                  datasetVersions.map((datasetVersion) => (
                    <TableRow key={datasetVersion.id}>
                      <TableCell>{datasetVersion.version}</TableCell>
                      <TableCell>{datasetVersion.modifications ?? "—"}</TableCell>
                      <TableCell>{datasetVersion.filePath ?? "—"}</TableCell>
                      <TableCell>{datasetVersion.submittingUser.name}</TableCell>
                      <TableCell>{new Date(datasetVersion.createdAt).toLocaleDateString("pt-BR")}</TableCell>
                      <TableCell>
                        <Button variant="outline" size="sm" onClick={() => handleDownload(datasetVersion.id)}>
                          <Download className="size-4" />Download
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </main>
  );
}
