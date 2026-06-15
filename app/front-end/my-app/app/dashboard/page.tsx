"use client";

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
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Spinner } from "@/components/ui/spinner";
import { Layers, Plus } from "lucide-react";
import { useRouter } from "next/dist/client/components/navigation";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function DashboardPage() {
  const [datasets, setDatasets] = useState<IDataset[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    datasetModel
      .listDatasets()
      .then((res) => setDatasets(res.data))
      .catch(() => setError("Falha ao carregar datasets."))
      .finally(() => setLoading(false));
  }, []);

  const router = useRouter();

  const handleButton = (data: IDataset) => {
    router.push(`/dataset-versions?datasetId=${data.id}&datasetName=${encodeURIComponent(data.name)}`);
  };

  return (
    <main style={{ padding: "20px" }}>
      <h1 className="text-2xl font-bold mb-4">Bem vindo(a) ao dashboard</h1>

      <Card>
        <CardHeader>
          <CardTitle>Datasets</CardTitle>
          <CardAction>
            <Button asChild>
              <Link href="/dashboard/datasets/create"><Plus className="size-4" />Novo Dataset</Link>
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
                        <Button variant="outline" size="sm" onClick={() => handleButton(dataset)}>
                          <Layers className="size-4" />Ver versões
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
