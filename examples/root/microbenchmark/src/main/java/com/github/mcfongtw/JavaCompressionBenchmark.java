package com.github.mcfongtw;

import com.ning.compress.lzf.LZFDecoder;
import com.ning.compress.lzf.LZFEncoder;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;
import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.io.InputStream;

public class JavaCompressionBenchmark {

    public static Logger LOG = LoggerFactory.getLogger(JavaCompressionBenchmark.class);

    public static final int NUM_OF_ITERATIONS = 10;

    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        public final String CORPUS_URL_TEXT = "corpus/urls.10K";

        public final String CORPUS_URL_GPB = "corpus/geo.protodata";

        public final String CORPUS_URL_IMAGE = "corpus/fireworks.jpeg";
    }
    

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureSnappyWithTextCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_TEXT);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            byte[] compressedTemp = Snappy.compress(uncompressed);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus urls.10K", new Object[]{compressedTemp.length, finLength});

            byte[] decompressedTemp = Snappy.uncompress(compressedTemp);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTemp.length, uncompressed.length});

            assert decompressedTemp.length == uncompressed.length;
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureSnappyWithGpbCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_GPB);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            byte[] compressedTemp = Snappy.compress(uncompressed);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus geo.protodata", new Object[]{compressedTemp.length, finLength});

            byte[] decompressedTemp = Snappy.uncompress(compressedTemp);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTemp.length, uncompressed.length});

            assert decompressedTemp.length == uncompressed.length;
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureSnappyWithImageCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_IMAGE);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            byte[] compressedTemp = Snappy.compress(uncompressed);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus fireworks.jpeg", new Object[]{compressedTemp.length, finLength});

            byte[] decompressedTemp = Snappy.uncompress(compressedTemp);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTemp.length, uncompressed.length});

            assert decompressedTemp.length == uncompressed.length;
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureLz4WithTextCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_TEXT);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            LZ4Factory factory = LZ4Factory.fastestInstance();
            LZ4Compressor compressor = factory.fastCompressor();

            int maxCompressedLength = compressor.maxCompressedLength(uncompressed.length);

            byte[] compressedTemp = new byte[maxCompressedLength];

            int compressedTempLen = compressor.compress(uncompressed, 0, uncompressed.length, compressedTemp, 0, maxCompressedLength);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus urls.10K", new Object[]{compressedTempLen, finLength});

            byte[] decompressedTemp = new byte[uncompressed.length];
            LZ4SafeDecompressor decompressor = factory.safeDecompressor();
            int decompressedTempLen = decompressor.decompress(compressedTemp, 0, compressedTempLen, decompressedTemp, 0);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTempLen, uncompressed.length});

            assert decompressedTempLen == uncompressed.length;
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureLz4WithGpbCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_GPB);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            LZ4Factory factory = LZ4Factory.fastestInstance();
            LZ4Compressor compressor = factory.fastCompressor();

            int maxCompressedLength = compressor.maxCompressedLength(uncompressed.length);

            byte[] compressedTemp = new byte[maxCompressedLength];

            int compressedTempLen = compressor.compress(uncompressed, 0, uncompressed.length, compressedTemp, 0, maxCompressedLength);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus geo.protodata", new Object[]{compressedTempLen, finLength});

            byte[] decompressedTemp = new byte[uncompressed.length];
            LZ4SafeDecompressor decompressor = factory.safeDecompressor();
            int decompressedTempLen = decompressor.decompress(compressedTemp, 0, compressedTempLen, decompressedTemp, 0);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTempLen, uncompressed.length});

            assert decompressedTempLen == uncompressed.length;
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureLz4WithImageCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_IMAGE);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            LZ4Factory factory = LZ4Factory.fastestInstance();
            LZ4Compressor compressor = factory.fastCompressor();

            int maxCompressedLength = compressor.maxCompressedLength(uncompressed.length);

            byte[] compressedTemp = new byte[maxCompressedLength];

            int compressedTempLen = compressor.compress(uncompressed, 0, uncompressed.length, compressedTemp, 0, maxCompressedLength);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus fireworks.jpeg", new Object[]{compressedTempLen, finLength});

            byte[] decompressedTemp = new byte[uncompressed.length];
            LZ4SafeDecompressor decompressor = factory.safeDecompressor();
            int decompressedTempLen = decompressor.decompress(compressedTemp, 0, compressedTempLen, decompressedTemp, 0);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTempLen, uncompressed.length});

            assert decompressedTempLen == uncompressed.length;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureLzfWithTextCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_TEXT);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            byte[] compressedTemp = LZFEncoder.encode(uncompressed);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus urls.10K", new Object[]{compressedTemp.length, finLength});

            byte[] decompressedTemp = LZFDecoder.decode(compressedTemp);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTemp.length, finLength});

            assert decompressedTemp.length == finLength;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureLzfWithGpbCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_GPB);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            byte[] compressedTemp = LZFEncoder.encode(uncompressed);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus urls.10K", new Object[]{compressedTemp.length, finLength});

            byte[] decompressedTemp = LZFDecoder.decode(compressedTemp);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTemp.length, finLength});

            assert decompressedTemp.length == finLength;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations=NUM_OF_ITERATIONS)
    public void measureLzfWithImageCorpus(ExecutionPlan plan) throws IOException {
        try(
                InputStream fin = getClass().getClassLoader().getResourceAsStream(plan.CORPUS_URL_IMAGE);
        ) {
            byte[] uncompressed = IOUtils.toByteArray(fin);

            int finLength = uncompressed.length;

            fin.read(uncompressed);

            byte[] compressedTemp = LZFEncoder.encode(uncompressed);

            LOG.debug("Compression Ratio [{}] / [{}] bytes w/ corpus urls.10K", new Object[]{compressedTemp.length, finLength});

            byte[] decompressedTemp = LZFDecoder.decode(compressedTemp);

            LOG.debug("Decompression [{}] == [{}] bytes", new Object[]{decompressedTemp.length, finLength});

            assert decompressedTemp.length == finLength;
        }
    }

    public static void main(String[] args) throws RunnerException {
        //TODO: Need to recreate table via command line:
        //curl -XPOST 'http://localhost:8086/query' --data-urlencode 'q=DROP DATABASE "demo"'
        //curl -XPOST 'http://localhost:8086/query' --data-urlencode 'q=CREATE DATABASE "demo"'
        Options opt = new OptionsBuilder()
                .include(JavaCompressionBenchmark.class.getSimpleName())
                .detectJvmArgs()
                .warmupIterations(10)
                .addProfiler(GCProfiler.class)
                .forks(1)
                .resultFormat(ResultFormatType.JSON)
                .verbosity(VerboseMode.EXTRA)
                .result("JavaCompressionBenchmark-result.json")
                .build();

        new Runner(opt).run();
    }
}
