"use client";

import * as React from "react";
import { useEffect, useState } from "react";
import { Bar, BarChart, CartesianGrid, XAxis } from "recharts";
import { datasetStatsModel } from "@/app/api/dataset-stats/datasetStatsModel";
import {
  IDatasetActivityPoint,
  IDatasetActivitySummary,
  IHourlyActivityPoint,
} from "@/app/api/types";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  ChartConfig,
  ChartContainer,
  ChartLegend,
  ChartLegendContent,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { Spinner } from "@/components/ui/spinner";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

const chartConfig = {
  downloads: { label: "Downloads", color: "var(--chart-1)" },
  accesses: { label: "Acessos", color: "var(--chart-2)" },
} satisfies ChartConfig;

// "day" comes as a plain "YYYY-MM-DD" string (no time component), so we format
// it by hand instead of going through Date/toLocaleDateString — parsing a
// date-only string with `new Date()` treats it as UTC midnight and shifts by
// a day once rendered in a negative-offset timezone like pt-BR.
function formatDayShort(value: string) {
  const [, month, day] = value.split("-");
  return `${day}/${month}`;
}

function formatDayFull(label: React.ReactNode) {
  const [year, month, day] = String(label).split("-");
  return `${day}/${month}/${year}`;
}

function formatHour(value: number) {
  return `${String(value).padStart(2, "0")}h`;
}

function formatHourLabel(label: React.ReactNode) {
  return formatHour(Number(label));
}

interface DatasetReportsProps {
  datasetCount: number;
}

export function DatasetReports({ datasetCount }: DatasetReportsProps) {
  const [summary, setSummary] = useState<IDatasetActivitySummary[]>([]);
  const [daily, setDaily] = useState<IDatasetActivityPoint[]>([]);
  const [hourly, setHourly] = useState<IHourlyActivityPoint[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    Promise.all([
      datasetStatsModel.getActivitySummary(),
      datasetStatsModel.getDailyActivity(30),
      datasetStatsModel.getHourlyActivity(),
    ])
      .then(([summaryRes, dailyRes, hourlyRes]) => {
        setSummary(summaryRes.data);
        setDaily(dailyRes.data);
        setHourly(hourlyRes.data);
      })
      .catch(() => setError("Falha ao carregar relatórios."))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <Card className="mb-6">
        <CardContent className="py-6">
          <Spinner className="mx-auto size-5" />
        </CardContent>
      </Card>
    );
  }

  if (error) {
    return (
      <Card className="mb-6">
        <CardContent className="py-6">
          <p style={{ color: "red" }}>{error}</p>
        </CardContent>
      </Card>
    );
  }

  const totalDownloads = summary.reduce((acc, item) => acc + item.totalDownloads, 0);
  const totalAccesses = summary.reduce((acc, item) => acc + item.totalAccesses, 0);
  const topDatasets = summary.slice(0, 5);

  return (
    <div className="mb-6 flex flex-col gap-4">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
        <Card>
          <CardHeader>
            <CardDescription>Total de datasets</CardDescription>
            <CardTitle className="text-2xl">{datasetCount}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader>
            <CardDescription>Total de downloads</CardDescription>
            <CardTitle className="text-2xl">{totalDownloads}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader>
            <CardDescription>Total de acessos</CardDescription>
            <CardTitle className="text-2xl">{totalAccesses}</CardTitle>
          </CardHeader>
        </Card>
      </div>

      <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Atividade diária (últimos 30 dias)</CardTitle>
            <CardDescription>Downloads e acessos por dia</CardDescription>
          </CardHeader>
          <CardContent>
            <ChartContainer config={chartConfig} className="aspect-auto h-[160px] w-full">
              <BarChart accessibilityLayer data={daily}>
                <CartesianGrid vertical={false} />
                <XAxis
                  dataKey="day"
                  tickLine={false}
                  axisLine={false}
                  tickMargin={8}
                  minTickGap={24}
                  tickFormatter={formatDayShort}
                />
                <ChartTooltip
                  content={<ChartTooltipContent labelFormatter={formatDayFull} />}
                />
                <ChartLegend content={<ChartLegendContent />} />
                <Bar dataKey="downloads" fill="var(--color-downloads)" radius={4} />
                <Bar dataKey="accesses" fill="var(--color-accesses)" radius={4} />
              </BarChart>
            </ChartContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Distribuição por hora do dia</CardTitle>
            <CardDescription>Downloads e acessos por hora, todo o período</CardDescription>
          </CardHeader>
          <CardContent>
            <ChartContainer config={chartConfig} className="aspect-auto h-[160px] w-full">
              <BarChart accessibilityLayer data={hourly}>
                <CartesianGrid vertical={false} />
                <XAxis
                  dataKey="hour"
                  tickLine={false}
                  axisLine={false}
                  tickMargin={8}
                  tickFormatter={formatHour}
                />
                <ChartTooltip
                  content={<ChartTooltipContent labelFormatter={formatHourLabel} />}
                />
                <ChartLegend content={<ChartLegendContent />} />
                <Bar dataKey="downloads" fill="var(--color-downloads)" radius={4} />
                <Bar dataKey="accesses" fill="var(--color-accesses)" radius={4} />
              </BarChart>
            </ChartContainer>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Datasets mais acessados</CardTitle>
          <CardDescription>Top 5 por downloads + acessos</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nome</TableHead>
                <TableHead>Downloads</TableHead>
                <TableHead>Acessos</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {topDatasets.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={3} className="text-center text-muted-foreground">
                    Nenhuma atividade registrada.
                  </TableCell>
                </TableRow>
              ) : (
                topDatasets.map((item) => (
                  <TableRow key={item.datasetId}>
                    <TableCell>{item.datasetName}</TableCell>
                    <TableCell>{item.totalDownloads}</TableCell>
                    <TableCell>{item.totalAccesses}</TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
