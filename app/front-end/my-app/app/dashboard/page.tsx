"use client";

import { useEffect, useState } from "react";
import { datasetModel } from "@/app/api/dataset/datasetModel";
import { IDataset } from "@/app/api/types";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/dist/client/components/navigation";
import { useForm } from "react-hook-form";

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
    router.push(`/dataset-versions?datasetId=${data.id}`);
  };

  return (
    <main style={{ padding: "20px" }}>
      <h1>Welcome to your Dashboard</h1>

      {loading && <p>Carregando datasets...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {!loading && !error && (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Descrição</TableHead>
              <TableHead>Origem</TableHead>
              <TableHead>Criador</TableHead>
              <TableHead>Criado em</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {datasets.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} className="text-center text-muted-foreground">
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
                    <Button onClick={() => handleButton(dataset)}>Ver Versões</Button>
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
