"use client";

import { IDatasetVersionFeature } from "@/app/api/types";
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
import { ArrowLeft, Download, Plus } from "lucide-react";
import { useRouter } from "next/dist/client/components/navigation";
import { use, useEffect, useState } from "react";
import { datasetVersionFeatureModel } from "../api/dataset-version-feature/datasetVersionFeatureModel";

export default function DatasetVersionsPage({
  searchParams,
}: {
  searchParams: Promise<{datasetId: string; datasetName: string; datasetVersionId: string; datasetVersionName: string }>;
}) {
  const {datasetId, datasetName, datasetVersionId, datasetVersionName } = use(searchParams);
  const router = useRouter();

  const [datasetVersionFeatures, setDatasetVersionFeatures] = useState<IDatasetVersionFeature[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    datasetVersionFeatureModel
      .listDatasetVersionFeatures(datasetVersionId)
      .then((res) => setDatasetVersionFeatures(res.data))
      .catch(() => setError("Falha ao carregar features da versão do dataset."))
      .finally(() => setLoading(false));
  }, [datasetVersionId, refreshKey]);

  const refresh = () => {
    setLoading(true);
    setRefreshKey((k) => k + 1);
  };

  return (
    <main style={{ padding: "20px" }}>
      <Card>
        <CardHeader>
          <CardTitle>{decodeURIComponent(datasetVersionName ?? "Features da Versão do Dataset")}</CardTitle>
          <CardAction className="flex gap-2">
            <Button variant="outline" onClick={() => router.push(`/dataset-versions?datasetId=${datasetId}&datasetName=${encodeURIComponent(datasetName)}`)}>
              <ArrowLeft className="size-4" />Voltar ao Dataset
            </Button>
          </CardAction>
        </CardHeader>
        <CardContent>
          {error && <p style={{ color: "red" }}>{error}</p>}

          {!error && (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Nome</TableHead>
                  <TableHead>Descrição</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading ? (
                  <TableRow>
                    <TableCell colSpan={5} className="text-center py-6">
                      <Spinner className="mx-auto size-5" />
                    </TableCell>
                  </TableRow>
                ) : datasetVersionFeatures.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={5} className="text-center text-muted-foreground">
                      Nenhuma feature encontrada para esta versão do dataset.
                    </TableCell>
                  </TableRow>
                ) : (
                  datasetVersionFeatures.map((feature) => (
                    <TableRow key={feature.name}>
                      <TableCell>{feature.name}</TableCell>
                      <TableCell>{feature.description ?? "—"}</TableCell>
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
