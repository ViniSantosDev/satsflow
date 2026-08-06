#!/bin/bash
# Script para compilar o projeto Satsflow

cd "$(dirname "$0")"

echo "╔═══════════════════════════════════════╗"
echo "║  Compilando Satsflow...              ║"
echo "╚═══════════════════════════════════════╝"

# Limpando build anterior
echo "🧹 Limpando builds anteriores..."
./gradlew clean --no-daemon

# Compilando com verbose
echo ""
echo "📦 Compilando Kotlin..."
./gradlew compileKotlin --no-daemon --stacktrace

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Compilação bem-sucedida!"
    echo ""
    echo "📌 Próximos passos:"
    echo "   1. ./gradlew bootRun (para dev)"
    echo "   2. docker-compose up (para prod)"
else
    echo ""
    echo "❌ Erro na compilação!"
    exit 1
fi

