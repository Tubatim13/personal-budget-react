import React, { useEffect, useState, useRef } from "react";
import { Pie } from "react-chartjs-2";
import * as d3 from "d3";
import "chart.js/auto";

const BudgetChart = () => {
  const [budgetData, setBudgetData] = useState([]);
  const d3ChartRef = useRef(null);

  useEffect(() => {
    fetch("/budget_data.json") // Fetch from public folder
      .then((response) => response.json())
      .then((data) => setBudgetData(data.myBudget))
      .catch((error) => console.error("Error loading budget data:", error));
  }, []);

  // Chart.js Data
  const chartData = {
    labels: budgetData.map((item) => item.title),
    datasets: [
      {
        data: budgetData.map((item) => item.budget),
        backgroundColor: [
          "#ffcd56",
          "#ff6384",
          "#36a2eb",
          "#fd6b19",
          "#4bc0c0",
          "#9966ff",
          "#c9cbcf",
        ],
      },
    ],
  };

  // D3.js Chart Rendering
  useEffect(() => {
    if (budgetData.length > 0) {
      d3.select(d3ChartRef.current).selectAll("*").remove();

      const width = 600, height = 600, radius = 200;
      const svg = d3
        .select(d3ChartRef.current)
        .append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", `translate(${width / 2},${height / 2})`);

      const color = d3.scaleOrdinal([
        "#8B4513", "#A0522D", "#CD853F", "#D2691E", "#B8860B", "#DAA520", "#556B2F"
      ]);

      const pie = d3.pie().value((d) => d.budget);
      const arc = d3.arc().outerRadius(radius * 0.7).innerRadius(radius * 0.4);
      const outerArc = d3.arc().innerRadius(radius * 0.9).outerRadius(radius * 0.9);

      const g = svg.selectAll(".arc")
        .data(pie(budgetData))
        .enter()
        .append("g")
        .attr("class", "arc");

      g.append("path")
        .attr("d", arc)
        .style("fill", (d) => color(d.data.title));

      svg.selectAll(".label")
        .data(pie(budgetData))
        .enter()
        .append("text")
        .attr("transform", (d) => {
          const pos = outerArc.centroid(d);
          pos[0] = radius * (midAngle(d) < Math.PI ? 0.9 : -0.9);
          return `translate(${pos})`;
        })
        .attr("dy", ".35em")
        .style("text-anchor", (d) => (midAngle(d) < Math.PI ? "start" : "end"))
        .style("fill", "#333")
        .style("font-size", "14px")
        .text((d) => d.data.title);

      svg.selectAll(".line")
        .data(pie(budgetData))
        .enter()
        .append("polyline")
        .attr("points", (d) => {
          const posA = arc.centroid(d);
          const posB = outerArc.centroid(d);
          const posC = [...posB];
          posC[0] = radius * (midAngle(d) < Math.PI ? 0.85 : -0.85);
          return [posA, posB, posC];
        })
        .style("fill", "none")
        .style("stroke", "black")
        .style("stroke-width", "1px")
        .style("opacity", 0.7);
    }
  }, [budgetData]);

  function midAngle(d) {
    return d.startAngle + (d.endAngle - d.startAngle) / 2;
  }

  return (
    <div>
      <h2>Budget Charts</h2>
      <div style={{ width: "400px", margin: "auto" }}>
        <Pie data={chartData} />
      </div>
      <div ref={d3ChartRef} style={{ textAlign: "center", marginTop: "20px" }} />
    </div>
  );
};

export default BudgetChart;
