"use client";

import { datasetModel } from "@/app/api/dataset/datasetModel";
import { IDataset } from "@/app/api/types";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
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

  return (
    <main style={{ padding: "20px" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" }}>
        <h1>Bem vindo(a) ao dashboard</h1>
        <Button asChild>
          <Link href="/dashboard/datasets/create">Novo Dataset</Link>
        </Button>
      </div>

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
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      )}
    </main>
  );
}
