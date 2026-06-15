"use client";

import { useEffect, useState } from "react";
import { datasetVersionModel } from "../api/dataset-version/datasetVersionModel";
import { IDatasetVersion } from "@/app/api/types";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import React from "react";
import { Button } from "@/components/ui/button";
import { datasetVersionDownloadModel } from "../api/dataset-version-download/datasetVersionDownloadModel";

export default function DatasetVersionsPage({searchParams}: Promise<{searchParams: {datasetId: string}}>) {
  const [datasetVersions, setDatasetVersions] = useState<IDatasetVersion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const {datasetId} = React.use(searchParams);
  useEffect(() => {
    datasetVersionModel
      .listByDatasetId(datasetId)
      .then((res) => setDatasetVersions(res.data))
      .catch(() => setError("Falha ao carregar versões do dataset."))
      .finally(() => setLoading(false));
  }, []);

  const handleDownload = async (datasetVersionId: string) => {
    try {
      const blob = await datasetVersionDownloadModel.downloadDatasetVersion(datasetVersionId);
      const url = window.URL.createObjectURL(blob);
      
      const link = document.createElement('a');
      link.href = url;
      link.download = 'example.csv'; // Desired filename
      document.body.appendChild(link);
      link.click();
      
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Download failed:', error);
    }
  };

  return (
    <main style={{ padding: "20px" }}>
      <h1>Dataset Versions</h1>

      {loading && <p>Carregando dataset...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {!loading && !error && (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Versão</TableHead>
              <TableHead>Modificações</TableHead>
              <TableHead>Path</TableHead>
              <TableHead>Criada por</TableHead>
              <TableHead>Criada em</TableHead>
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
                  <TableCell>{datasetVersion.version}</TableCell>
                  <TableCell>{datasetVersion.modifications ?? "—"}</TableCell>
                  <TableCell>{datasetVersion.filePath ?? "—"}</TableCell>
                  <TableCell>{datasetVersion.submittingUser.name}</TableCell>
                  <TableCell>{new Date(datasetVersion.createdAt).toLocaleDateString("pt-BR")}</TableCell>
                  <TableCell>
                    <Button onClick={() => handleDownload(datasetVersion.id)}>
                      Download
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      )}
    </main>
  );
}
