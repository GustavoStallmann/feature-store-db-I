"use client";

import "@xyflow/react/dist/style.css";
import {
  Background,
  Controls,
  Handle,
  Position,
  ReactFlow,
  type Edge,
  type Node,
  type NodeProps,
} from "@xyflow/react";
import { useMemo } from "react";
import { IDatasetVersion } from "@/app/api/types";

const HORIZONTAL_SPACING = 260;
const VERTICAL_SPACING = 160;

interface VersionNodeData {
  datasetVersion: IDatasetVersion;
  [key: string]: unknown;
}

type VersionNode = Node<VersionNodeData, "version">;

function VersionNodeComponent({ data }: NodeProps<VersionNode>) {
  const { datasetVersion } = data;

  return (
    <div className="w-56 overflow-hidden rounded-lg border border-border bg-card text-card-foreground shadow-sm">
      <Handle type="target" position={Position.Top} />
      <div className="flex items-center justify-between px-3 py-2">
        <span className="font-semibold">Versão {datasetVersion.version}</span>
        <span className="truncate text-xs text-muted-foreground">
          {datasetVersion.submittingUser.name}
        </span>
      </div>
      <Handle type="source" position={Position.Bottom} />
    </div>
  );
}

const nodeTypes = { version: VersionNodeComponent };

function layoutVersionTree(datasetVersions: IDatasetVersion[]) {
  const childrenByParentId = new Map<string, IDatasetVersion[]>();
  const roots: IDatasetVersion[] = [];
  const byVersionAsc = (a: IDatasetVersion, b: IDatasetVersion) => a.version - b.version;

  for (const datasetVersion of datasetVersions) {
    const parentId = datasetVersion.parentDatasetVersion?.id;
    const parentExists = parentId && datasetVersions.some((dv) => dv.id === parentId);

    if (parentExists) {
      const siblings = childrenByParentId.get(parentId) ?? [];
      siblings.push(datasetVersion);
      childrenByParentId.set(parentId, siblings);
    } else {
      roots.push(datasetVersion);
    }
  }

  roots.sort(byVersionAsc);
  for (const siblings of childrenByParentId.values()) siblings.sort(byVersionAsc);

  const positions = new Map<string, { x: number; y: number }>();
  let nextLeafSlot = 0;

  function placeNode(datasetVersion: IDatasetVersion, depth: number): number {
    const children = childrenByParentId.get(datasetVersion.id) ?? [];
    let x: number;

    if (children.length === 0) {
      x = nextLeafSlot * HORIZONTAL_SPACING;
      nextLeafSlot += 1;
    } else {
      const childXs = children.map((child) => placeNode(child, depth + 1));
      x = (Math.min(...childXs) + Math.max(...childXs)) / 2;
    }

    positions.set(datasetVersion.id, { x, y: depth * VERTICAL_SPACING });
    return x;
  }

  for (const root of roots) placeNode(root, 0);

  return positions;
}

export function VersionTreeView({
  datasetVersions,
}: {
  datasetVersions: IDatasetVersion[];
}) {
  const { nodes, edges } = useMemo(() => {
    const positions = layoutVersionTree(datasetVersions);

    const nodes: VersionNode[] = datasetVersions.map((datasetVersion) => ({
      id: datasetVersion.id,
      type: "version",
      position: positions.get(datasetVersion.id) ?? { x: 0, y: 0 },
      data: { datasetVersion },
    }));

    const edges: Edge[] = datasetVersions
      .filter((datasetVersion) => {
        const parentId = datasetVersion.parentDatasetVersion?.id;
        return parentId && datasetVersions.some((dv) => dv.id === parentId);
      })
      .map((datasetVersion) => {
        const parentId = datasetVersion.parentDatasetVersion!.id;
        return {
          id: `${parentId}-${datasetVersion.id}`,
          source: parentId,
          target: datasetVersion.id,
          type: "smoothstep",
        };
      });

    return { nodes, edges };
  }, [datasetVersions]);

  return (
    <div className="h-[600px] w-full overflow-hidden rounded-md border border-border">
      <ReactFlow
        nodes={nodes}
        edges={edges}
        nodeTypes={nodeTypes}
        colorMode="dark"
        fitView
        fitViewOptions={{ maxZoom: 0.75 }}
        minZoom={0.2}
        maxZoom={1}
        nodesDraggable={false}
        nodesConnectable={false}
      >
        <Background />
        <Controls showInteractive={false} />
      </ReactFlow>
    </div>
  );
}
