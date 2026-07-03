"use client";

import { useState, useEffect } from "react";
import { Layers, Plus } from "lucide-react";
import { useRouter } from "next/dist/client/components/navigation";
import { datasetModel } from "@/app/api/dataset/datasetModel";
import { IDataset } from "@/app/api/types";
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
import { CreateDatasetDialog } from "./_components/CreateDatasetDialog";
import { UpdateDatasetDialog } from "./_components/UpdateDatasetDialog";

export default function DashboardPage() {
  const router = useRouter();

  const [datasets, setDatasets] = useState<IDataset[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [createDialogOpen, setCreateDialogOpen] = useState(false);
  const [updateDialogOpen, setUpdateDialogOpen] = useState(false);
  const [selectedDatasetId, setSelectedDatasetId] = useState<string | null>(null);
  const [updateName, setUpdateName] = useState<string | null>(null);
  const [updateDescription, setUpdateDescription] = useState<string | null>(null);
  const [updateOrigin, setUpdateOrigin] = useState<string | null>(null);
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    datasetModel
      .listDatasets()
      .then((res) => setDatasets(res.data))
      .catch(() => setError("Falha ao carregar datasets."))
      .finally(() => setLoading(false));
  }, [refreshKey]);

  const refresh = () => {
    setLoading(true);
    setRefreshKey((k) => k + 1);
  };

  return (
    <main style={{ padding: "20px" }}>
      <h1 className="text-2xl font-bold mb-4">Bem vindo(a) ao dashboard</h1>

      <CreateDatasetDialog
        open={createDialogOpen}
        onOpenChange={setCreateDialogOpen}
        onSuccess={refresh}
      />
      <UpdateDatasetDialog
        id={selectedDatasetId}
        open={updateDialogOpen}
        onOpenChange={setUpdateDialogOpen}
        onSuccess={refresh}
        name={updateName}
        description={updateDescription}
        origin={updateOrigin}
      />

      <Card>
        <CardHeader>
          <CardTitle>Datasets</CardTitle>
          <CardAction>
            <Button onClick={() => setCreateDialogOpen(true)}>
              <Plus className="size-4" />Novo Dataset
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
                  <TableHead>Origem</TableHead>
                  <TableHead>Criador</TableHead>
                  <TableHead>Criado em</TableHead>
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
                ) : datasets.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center text-muted-foreground">
                      Nenhum dataset encontrado.
                    </TableCell>
                  </TableRow>
                ) : (
                  datasets.map((dataset) => (
                    <TableRow key={dataset.id}>
                      <TableCell>{dataset.name}</TableCell>
                      <TableCell>{dataset.description ?? "—"}</TableCell>
                      <TableCell>{dataset.origin ?? "—"}</TableCell>
                      <TableCell>{dataset.creatorUser.name}</TableCell>
                      <TableCell>{new Date(dataset.createdAt).toLocaleDateString("pt-BR")}</TableCell>
                      <TableCell>
                        <Button variant="outline" size="sm" onClick={() => router.push(`/dataset-versions?datasetId=${dataset.id}&datasetName=${encodeURIComponent(dataset.name)}`)}>
                          <Layers className="size-4" />Ver versões
                        </Button>
                      </TableCell>
                      <TableCell>
                        <Button variant="outline" size="sm" onClick={() => {
                          setSelectedDatasetId(dataset.id);
                          setUpdateName(dataset.name);
                          setUpdateDescription(dataset.description ?? "");
                          setUpdateOrigin(dataset.origin ?? "");
                          setUpdateDialogOpen(true);
                        }}>
                          <Plus className="size-4" />Atualizar
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
